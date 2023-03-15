package com.ranga.spark.streaming.graceful.shutdown

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.streaming._

// nc -lk 9999
object SparkStreamingGracefulShutdownApp extends App with Serializable {

  private val appName = getClass.getSimpleName.replace("$", "") // App Name
  @transient private lazy val logger: Logger = Logger.getLogger(appName)

  private val baseDirPath = "/tmp/streaming/graceful_shutdown_app"    // Base directory
  private val markerFile = s"$baseDirPath/marker_file"                // Marker File
  private val checkpointDirectory = s"$baseDirPath/checkpoint"        // Checkpoint Directory
  private val batchInterval = 30 * 1000                               // 30 seconds batch interval

  if (args.length < 2) {
    logger.error(s"Usage\t: $appName <hostname> <port>")
    logger.info(s"Example\t: $appName localhost 9999")
    System.exit(1)
  }

  private def createContext(hostname: String, port: Int): StreamingContext = {

    // Creating the SparkConf object
    val sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[2]")

    // Creating the StreamingContext object
    logger.info(s"Creating StreamingContext with duration $batchInterval milli seconds ...")
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

  private def stopByMarkerFile(ssc: StreamingContext, markerFile: String): Unit = {
    var isStop = false
    val timeout = batchInterval * 2
    while (!isStop) {
      logger.info(s"Calling awaitTerminationOrTimeout() with timeout $timeout milli seconds ...")
      isStop = ssc.awaitTerminationOrTimeout(timeout) // Wait for the computation to terminate
      if (isStop)
        logger.info("Spark Streaming context is terminated. Exiting application ...")
      else
        logger.info("Spark Streaming Application is still running ...")

      if (!isStop && checkMarkerFileExists()) {
        logger.info("Stopping the Spark Streaming Context Gracefully after 1 second ...")
        Thread.sleep(1000)
        ssc.stop(stopSparkContext = true, stopGracefully = true)
        deleteMarkerFile()
        logger.info("Spark Streaming Context is Stopped ...")
      }
    }

    def checkMarkerFileExists(): Boolean = {
      val path = new Path(markerFile)
      val fs = path.getFileSystem(new Configuration())
      fs.exists(path)
    }

    def deleteMarkerFile(): Unit = {
      val path = new Path(markerFile)
      val fs = path.getFileSystem(new Configuration())
      if (fs.exists(path)) {
        fs.delete(path, true)
        logger.info("MarkerFile successfully deleted")
      }
    }
  }

  // Get StreamingContext from checkpoint data or create a new one
  private val Array(hostname, port) = args
  logger.info(s"Hostname $hostname and Port $port ...")

  private val ssc = StreamingContext.getOrCreate(checkpointDirectory, () =>  createContext(hostname, port.toInt))
  ssc.start()
  logger.info("StreamingContext Started ...")
  stopByMarkerFile(ssc, markerFile)
}