package com.ranga.spark.streaming.shutdown.util.marker

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.log4j.Logger
import org.apache.spark.streaming.StreamingContext

/**
 * Utility class to Shutdown the SparkStreaming Application by using Marker FileSystem Approach
 *
 * @author Ranga Reddy
 * @created 8-Jun-2023
 * @version 1.0
 */
object StopByMarkerFileSystem {

  // Create a logger instance for logging messages
  @transient private lazy val logger: Logger = Logger.getLogger(getClass.getSimpleName.replace("$", ""))

  /**
   * Stops the Spark Streaming context gracefully based on the presence of a marker file.
   *
   * @param ssc           The StreamingContext to stop
   * @param markerFile    The path of the marker file indicating when to stop
   * @param batchInterval The batch interval of the streaming job in seconds
   */
  def stopByMarkerFile(ssc: StreamingContext, markerFile: String, batchInterval: Long = 3 * 1000): Unit = {
    var isStop = false

    while (!isStop) {
      logger.info(s"Calling awaitTerminationOrTimeout() with timeout $batchInterval seconds ...")

      // Wait for the computation to terminate within the specified timeout
      isStop = ssc.awaitTerminationOrTimeout(batchInterval)

      if (isStop) {
        logger.info("Spark Streaming context is terminated. Exiting application ...")
      } else {
        logger.info("Spark Streaming Application is still running ...")
      }

      if (!isStop && checkMarkerFileExists(markerFile)) {
        logger.info("Stopping the Spark Streaming Context Gracefully after 1 second ...")

        // Sleep for 1 second to allow any pending batches to complete
        Thread.sleep(1000)

        // Stop the streaming context if it has not already terminated
        ssc.stop(stopSparkContext = true, stopGracefully = true)

        // Delete the marker file
        deleteMarkerFile(markerFile)

        logger.info("Spark Streaming Context is Stopped ...")
      }
    }
  }

  /**
   * Checks if a marker file exists in the file system.
   *
   * @param markerFile The path of the marker file
   * @return True if the marker file exists, false otherwise
   */

  private def checkMarkerFileExists(markerFile: String): Boolean = {
    // Create a new Path object with the provided marker file path
    val path = new Path(markerFile)

    // Get the FileSystem associated with the path
    val fs = path.getFileSystem(new Configuration())

    // Check if the marker file exists in the file system
    fs.exists(path)
  }

  /**
   * Deletes a marker file from the file system.
   *
   * @param markerFile The path of the marker file to delete
   */
  private def deleteMarkerFile(markerFile: String): Unit = {
    // Create a new Path object with the provided marker file path
    val path = new Path(markerFile)

    // Get the FileSystem associated with the path
    val fs = path.getFileSystem(new Configuration())

    // Check if the marker file exists
    if (fs.exists(path)) {
      // Delete the marker file
      fs.delete(path, true)

      // Log a message indicating the successful deletion of the marker file
      logger.info(s"MarkerFile $markerFile successfully deleted")
    }
  }

}