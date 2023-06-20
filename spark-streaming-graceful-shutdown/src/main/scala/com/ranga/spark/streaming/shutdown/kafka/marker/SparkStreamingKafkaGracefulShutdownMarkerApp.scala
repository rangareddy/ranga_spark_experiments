package com.ranga.spark.streaming.shutdown.kafka.marker

import com.ranga.spark.streaming.shutdown.util.marker.StopByMarkerFileSystem
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}

/**
 * This class represents a Spark Streaming application that consumes messages from Kafka and gracefully shuts down
 * when a termination marker is received.
 *
 * The application utilizes the Spark Streaming and Kafka integration to consume messages from Kafka topics. It also
 * listens for a termination marker, which can be a specific message or an event that indicates the need for the
 * application to gracefully stop. Upon receiving the termination marker, the application finishes processing the
 * current batch and then stops the streaming context, ensuring a clean and controlled shutdown.
 */

object SparkStreamingKafkaGracefulShutdownMarkerApp extends App with Serializable {

  // Create a logger instance for logging messages
  @transient lazy val logger: Logger = Logger.getLogger(getClass.getName)

  // Define AppName
  private val appName = getClass.getSimpleName.replace("$", "")

  if (args.length < 4) {
    logger.error(s"Usage\t: $appName <bootstrapServers> <groupId> <topics> <markerFile>")
    logger.info(s"Example\t: $appName <localhost:9092> <my_group> <test_topic> </tmp/graceful_shutdown>")
    System.exit(1)
  }

  // Consume command line arguments
  private val Array(bootstrapServers, groupId, topic, markerFile) = args

  // Create a SparkConf object
  val sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[2]")

  // Create StreamingContext, with batch duration in seconds
  private val duration = if (args.length > 4) args(4).toInt else 30
  logger.info(s"Creating StreamingContext with batch duration $duration seconds...")
  private val batchDuration = Seconds(duration)
  val streamingContext = new StreamingContext(sparkConf, batchDuration)
  logger.info("StreamingContext created successfully ...")

  // Define Kafka Parameters
  private val kafkaParams = Map[String, Object](
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> bootstrapServers,
    ConsumerConfig.GROUP_ID_CONFIG -> groupId,
    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer]
  )

  // Create a set of topics from a string
  private val topics: Set[String] = topic.split(",").map(_.trim).toSet

  // Create a streaming context from Kafka
  private val stream = KafkaUtils.createDirectStream[String, String](
    streamingContext,
    LocationStrategies.PreferConsistent,
    ConsumerStrategies.Subscribe[String, String](topics, kafkaParams)
  )

  // Get the lines of text from Kafka
  val lines = stream.map(record => record.value())

  // Split lines into words
  val words = lines.flatMap(_.split(" "))

  // Map every word to a tuple
  private val wordMap = words.map(word => (word, 1))

  // Count occurrences of each word
  val wordCounts = wordMap.reduceByKey(_ + _)

  // Print the word count
  wordCounts.print()

  // Start stream processing
  streamingContext.start()
  logger.info("StreamingContext Started ...")

  // Gracefully shutdown the Spark Streaming application when the termination marker is received
  StopByMarkerFileSystem.stopByMarkerFile(streamingContext, markerFile)

  // Wait for the computation to terminate
  streamingContext.awaitTermination()
}