package com.ranga.spark.streaming.shutdown.kafka.marker

import com.ranga.spark.streaming.shutdown.kafka.hook.SparkStreamingKafkaGracefulShutdownHookApp.{appName, args, logger}
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import com.ranga.spark.streaming.shutdown.util.marker.StopByMarkerFileSystem
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}

// nc -lk 9999
object SparkStreamingKafkaGracefulShutdownMarkerApp extends App with Serializable {

  private val appName = getClass.getSimpleName.replace("$", "") // App Name
  // Create a logger instance for logging messages
  @transient private lazy val logger: Logger = Logger.getLogger(appName)

  if (args.length < 4) {
    logger.error(s"Usage\t: $appName <bootstrapServers> <groupId> <topics> <markerFile>")
    logger.info(s"Example\t: $appName <localhost:9092> <my_group> <test_topic> </tmp/graceful_shutdown>")
    System.exit(1)
  }

  // Get StreamingContext from checkpoint data or create a new one
  private val Array(bootstrapServers, groupId, topic, markerFile) = args

  // Creating the SparkConf object
  val sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[2]")

  // Create context with 3 second batch interval
  logger.info(s"Creating StreamingContext with duration 3 seconds batch interval ...")
  val streamingContext = new StreamingContext(sparkConf, Seconds(3))
  logger.info("StreamingContext created successfully ...")

  private val kafkaParams = Map[String, Object](
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> bootstrapServers,
    ConsumerConfig.GROUP_ID_CONFIG -> groupId,
    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer]
  )

  private val topics: Set[String] = topic.split(",").map(_.trim).toSet

  private val stream = KafkaUtils.createDirectStream[String, String](
    streamingContext,
    LocationStrategies.PreferConsistent,
    ConsumerStrategies.Subscribe[String, String](topics, kafkaParams)
  )

  // Get the lines, split them into words, count the words and print
  val lines = stream.map(record => record.value())
  val words = lines.flatMap(_.split(" "))
  val wordCounts = words.map(word => (word, 1)).reduceByKey(_ + _)
  wordCounts.print()

  // Start the computation
  streamingContext.start()
  logger.info("StreamingContext Started ...")

  StopByMarkerFileSystem.stopByMarkerFile(streamingContext, markerFile)
}