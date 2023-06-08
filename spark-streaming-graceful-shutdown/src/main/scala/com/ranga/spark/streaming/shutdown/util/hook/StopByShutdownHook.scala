package com.ranga.spark.streaming.shutdown.util.hook

import org.apache.log4j.Logger
import org.apache.spark.streaming.StreamingContext

/**
 * Utility class to Shutdown the SparkStreaming Application by using Shutdown Hook
 *
 * @author Ranga Reddy
 * @created 8-Jun-2023
 * @version 1.0
 */
object StopByShutdownHook {

  // Create a logger instance for logging messages
  @transient private lazy val logger: Logger = Logger.getLogger(getClass.getSimpleName.replace("$", ""))

  // Define a method to stop Spark Streaming by adding a shutdown hook
  def stopByShutdownHook(streamingContext: StreamingContext): Unit = {
    // Add a shutdown hook to gracefully stop the Spark Streaming application
    sys.addShutdownHook {
      // Log a message indicating the application is being stopped
      logger.info("Gracefully stopping Spark Streaming Application.")
      // Stop the Spark Streaming context, including the underlying SparkContext, gracefully
      streamingContext.stop(stopSparkContext = true, stopGracefully = true)
      // Log a message indicating the successful stopping of the application
      logger.info("The Spark Streaming Application has been successfully stopped.")
    }
  }
}

