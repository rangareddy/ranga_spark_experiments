package com.ranga.spark.streaming.graceful.shutdown.hook

import org.apache.log4j.Logger
import org.apache.spark.streaming.StreamingContext


object StopByShutdownHook {

  @transient private lazy val logger: Logger = Logger.getLogger(getClass.getSimpleName)

  def stopByShutdownHook(ssc: StreamingContext): Unit = {
    sys.addShutdownHook {
      logger.info("Gracefully stopping Spark Streaming Application")
      ssc.stop(stopSparkContext = true, stopGracefully = true)
      logger.info("Spark Streaming Application successfully stopped")
    }
  }
}
