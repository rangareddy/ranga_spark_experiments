package com.ranga.spark.streaming.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.SparkConf
import org.apache.log4j.Logger
import org.apache.spark.streaming._
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

object SparkStreamingKafkaIntegrationApp extends App with Serializable {

    @transient lazy val logger: Logger = Logger.getLogger(getClass.getName)

    if (args.length < 4) {
        logger.error("Usage   : SparkStreamingKafkaIntegrationApp <bootstrapServers> <groupId> <topics> <duration_in_seconds>")
        logger.info( "Example : SparkStreamingKafkaIntegrationApp <localhost:9092> <my_group> <test_topic> <30>")
        System.exit(1)
    }

    // Creating the SparkConf object
    val sparkConf = new SparkConf().setAppName("SparkStreamingKafkaIntegrationApp Example").setIfMissing("spark.master", "local[2]")

    val Array(bootstrapServers, groupId, topic, duration) = args
    val streamingContext = new StreamingContext(sparkConf, Seconds(duration.toInt))

    val topics: Set[String] = topic.split(",").map(_.trim).toSet

    val kafkaParams = Map[String, Object](
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> bootstrapServers,
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
        ConsumerConfig.GROUP_ID_CONFIG -> groupId,
        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> (false: java.lang.Boolean)
    )

    val stream = KafkaUtils.createDirectStream[String, String](
        streamingContext,
        PreferConsistent,
        Subscribe[String, String](topics, kafkaParams)
    )

    var offsetRanges = Array.empty[OffsetRange]

    stream.foreachRDD { rdd =>
        if(!rdd.isEmpty()) {
            val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
            val resultRDD = rdd.map(_.value())
            resultRDD.saveAsTextFile("")

            /*rdd.foreachPartition { iter =>
                val o: OffsetRange = offsetRanges(TaskContext.get.partitionId)
                println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
            }*/

            // Storing the offsets Kafka itself
            stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
        }
    }

    streamingContext.start()
    streamingContext.awaitTermination()
}