package com.ranga.spark.streaming.graceful.shutdown.marker

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.log4j.Logger
import org.apache.spark.streaming.StreamingContext

object StopByMarker {

  @transient private lazy val logger: Logger = Logger.getLogger(getClass.getSimpleName)

  def stopByMarkerFile(ssc: StreamingContext, markerFile: String, batchInterval: Long): Unit = {
    var isStop = false
    val timeout = batchInterval * 2
    while (!isStop) {
      logger.info(s"Calling awaitTerminationOrTimeout() with timeout $timeout milli seconds ...")
      isStop = ssc.awaitTerminationOrTimeout(timeout) // Wait for the computation to terminate
      if (isStop)
        logger.info("Spark Streaming context is terminated. Exiting application ...")
      else
        logger.info("Spark Streaming Application is still running ...")

      if (!isStop && checkMarkerFileExists(markerFile)) {
        logger.info("Stopping the Spark Streaming Context Gracefully after 1 second ...")
        Thread.sleep(1000)
        ssc.stop(stopSparkContext = true, stopGracefully = true)
        deleteMarkerFile(markerFile)
        logger.info("Spark Streaming Context is Stopped ...")
      }
    }
  }

  private def checkMarkerFileExists(markerFile: String): Boolean = {
    val path = new Path(markerFile)
    val fs = path.getFileSystem(new Configuration())
    fs.exists(path)
  }

  private def deleteMarkerFile(markerFile: String): Unit = {
    val path = new Path(markerFile)
    val fs = path.getFileSystem(new Configuration())
    if (fs.exists(path)) {
      fs.delete(path, true)
      logger.info(s"MarkerFile $markerFile successfully deleted")
    }
  }
}