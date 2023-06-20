package com.ranga.spark.streaming.shutdown.socket.http

import com.ranga.spark.streaming.shutdown.util.http.StopByHttpHandler
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.streaming._

/**
 * SparkStreamingSocketGracefulShutdownHttpApp is a Spark Streaming application that reads data from a socket stream
 * and performs word count computations. It gracefully shuts down the streaming context using an HTTP endpoint.
 *
 * Usage:
 *   - Start a socket server on localhost:9999 and feed data to it
 *   - Run the SparkStreamingSocketGracefulShutdownHttpApp program
 *   - The application will read data from the socket stream, perform word count computations, and print the results
 *   - To trigger the graceful shutdown, send an HTTP POST request to the specified endpoint ("/shutdown")
 *   - The streaming context will be stopped gracefully, and the application will exit
 *
 * Note: The HTTP endpoint can be accessed externally, so make sure to secure it properly in a production environment.
 */

// nc -lk 9999
object SparkStreamingSocketGracefulShutdownHttpApp extends App with Serializable {

  // Create a logger instance for logging messages
  @transient private lazy val logger: Logger = Logger.getLogger(getClass.getName)

  // Define AppName
  private val appName = getClass.getSimpleName.replace("$", "")

  // Checkpoint Directory
  private val checkpointDirectory = s"/tmp/streaming/$appName"

  if (args.length < 3) {
    logger.error(s"Usage\t: $appName <hostname> <port> <jetty_port>")
    logger.info(s"Example\t: $appName localhost 9999 3443")
    System.exit(1)
  }

  /**
   * Creates the StreamingContext with the given hostname and port.
   *
   * @param hostname The hostname of the socket stream
   * @param port     The port of the socket stream
   * @return The created StreamingContext
   */
  private def createContext(hostname: String, port: Int, duration: Long): StreamingContext = {

    // Creating the SparkConf object
    val sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[2]")

    // Creating the StreamingContext object
    val batchDuration = Seconds(duration)
    logger.info(s"Creating StreamingContext with batch duration $duration seconds...")

    val ssc = new StreamingContext(sparkConf, batchDuration)
    ssc.checkpoint(checkpointDirectory) // set checkpoint directory
    logger.info("StreamingContext created successfully ...")

    // Create a socket stream on target hostname:port
    val lines = ssc.socketTextStream(hostname, port)

    // Split each line into words
    val words = lines.flatMap(_.split(" "))

    // Count each word in each batch
    val pairs = words.map(word => (word, 1))
    val wordCounts = pairs.reduceByKey(_ + _)

    // Print the first ten elements of each RDD generated in this DStream to the console
    wordCounts.print()
    ssc
  }

  // Get StreamingContext from checkpoint data or create a new one
  private val Array(hostname, port, jettyPort) = args
  logger.info(s"Hostname $hostname and Port $port ...")

  private val duration: Long = if (args.length > 3) args(3).toInt else 30
  private val ssc = StreamingContext.getOrCreate(checkpointDirectory, () => createContext(hostname, port.toInt, duration))
  ssc.start()
  logger.info("StreamingContext Started ...")

  // Start the HTTP server that accepts the stop request
  StopByHttpHandler.httpServer(jettyPort.toInt, ssc, appName)

  // Wait for the task to terminate
  ssc.awaitTermination()
}