package com.ranga.spark.streaming.shutdown.socket.marker

import com.ranga.spark.streaming.shutdown.util.marker.StopByMarkerFileSystem
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.streaming._

// nc -lk 9999
object SparkStreamingGracefulShutdownMarkerApp extends App with Serializable {

  private val appName = getClass.getSimpleName.replace("$", "") // App Name
  // Create a logger instance for logging messages
  @transient private lazy val logger: Logger = Logger.getLogger(appName)

  private val markerFile = "/tmp/streaming/graceful_shutdown/marker_file" // Marker File
  private val checkpointDirectory = s"/tmp/streaming/$appName/checkpoint" // Checkpoint Directory
  private val batchInterval: Long = 30 * 1000 // 30 seconds batch interval

  if (args.length < 2) {
    logger.error(s"Usage\t: $appName <hostname> <port>")
    logger.info(s"Example\t: $appName localhost 9999")
    System.exit(1)
  }

  /**
   * Creates the StreamingContext with the given hostname and port.
   *
   * @param hostname The hostname of the socket stream
   * @param port     The port of the socket stream
   * @return The created StreamingContext
   */
  private def createContext(hostname: String, port: Int): StreamingContext = {

    // Creating the SparkConf object
    val sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[2]")

    // Creating the StreamingContext object
    logger.info(s"Creating StreamingContext with duration $batchInterval milliseconds ...")
    val ssc = new StreamingContext(sparkConf, Milliseconds(batchInterval))
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
  private val Array(hostname, port) = args
  logger.info(s"Hostname $hostname and Port $port ...")

  private val ssc = StreamingContext.getOrCreate(checkpointDirectory, () => createContext(hostname, port.toInt))
  ssc.start()
  logger.info("StreamingContext Started ...")
  StopByMarkerFileSystem.stopByMarkerFile(ssc, markerFile, batchInterval)
}