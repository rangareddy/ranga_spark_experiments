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
   * @param ssc         The StreamingContext to stop
   * @param markerFile  The path of the marker file indicating when to stop
   */
  def stopByMarkerFile(ssc: StreamingContext, markerFile: String): Unit = {
    var isStop = false
    while (!isStop) {
      if (!isStop && checkMarkerFileExists(markerFile)) {
        logger.info("Marker file exists, stopping the Spark Streaming Context gracefully ...")
        // Stop the streaming context if it has not already terminated
        ssc.stop(stopSparkContext = true, stopGracefully = true)
        // Delete the marker file
        deleteMarkerFile(markerFile)
        logger.info("Spark Streaming Context is Stopped ...")
        isStop = true
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
    val markerFileExists = fs.exists(path)
    if (markerFileExists)
      logger.info(s"MarkerFile $markerFile exists")
    markerFileExists
  }

  /**
   * Deletes a marker file from the file system.
   *
   * @param markerFile The path of the marker file to delete
   */
  private def deleteMarkerFile(markerFile: String): Unit = {
    logger.info(s"Deleting marker file: $markerFile")

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