package com.ranga.spark.streaming.shutdown.kafka.hook

import com.ranga.spark.streaming.shutdown.util.hook.StopByShutdownHook
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010._

/**
 * This class represents a Spark Streaming application that consumes messages from Kafka and gracefully shuts down
 * when a termination signal is received via a shutdown hook.
 *
 * The application utilizes the Spark Streaming and Kafka integration to consume messages from Kafka topics. It also
 * registers a shutdown hook to capture termination signals from the environment. Upon receiving a termination signal,
 * the application performs a graceful shutdown by finishing the processing of the current batch and then stopping the
 * streaming context.
 *
 * Different ways to send Shutdown signal
 * 1. Ctrl+C:: Pressing Ctrl+C on the command line where your application is running sends the SIGINT signal to
 * terminate the application. This is the most common method during manual termination.
 * 2. Using the kill command: The kill command allows you to send signals to processes. You can use it to send signals
 * like SIGTERM or SIGINT to gracefully terminate your Spark Streaming application.
 * Syntax: kill -SIGTERM <pid> etc
 * 3. Using the pkill command: The pkill command allows you to send signals to processes based on their names or patterns.
 * You can use it to send signals like SIGTERM or SIGINT to your Spark Streaming application by specifying the process
 * name or part of the name.
 * Syntax: pkill -<signal_number> -f <process_name>
 */

object SparkStreamingKafkaGracefulShutdownHookApp extends App with Serializable {

  // Create a logger instance for logging messages
  @transient private lazy val logger: Logger = Logger.getLogger(getClass.getName)

  // Define AppName
  private val appName = getClass.getSimpleName.replace("$", "")

  if (args.length < 3) {
    logger.error(s"Usage\t: $appName <bootstrapServers> <groupId> <topics>")
    logger.info(s"Example\t: $appName <localhost:9092> <my_group> <test_topic>")
    System.exit(1)
  }

  // Consume command line arguments
  private val Array(bootstrapServers, groupId, topic) = args

  // Create a SparkConf object
  val sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[2]")

  // Create StreamingContext, with batch duration in seconds
  private val duration = if (args.length > 3) args(3).toInt else 30
  private val batchDuration = Seconds(duration)
  logger.info(s"Creating StreamingContext with batch duration $duration seconds...")
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

  // Set up a shutdown hook to gracefully stop the StreamingContext
  StopByShutdownHook.stopByShutdownHook(streamingContext)

  // Wait for the computation to terminate
  streamingContext.awaitTermination()
}