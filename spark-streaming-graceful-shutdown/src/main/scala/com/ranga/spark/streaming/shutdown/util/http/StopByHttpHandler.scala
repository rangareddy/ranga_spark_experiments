package com.ranga.spark.streaming.shutdown.util.http

import org.apache.log4j.Logger
import org.apache.spark.streaming._
import org.spark_project.jetty.server.handler.{AbstractHandler, ContextHandler}
import org.spark_project.jetty.server.{Request, Server}

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

/**
 * Utility class to Shutdown the SparkStreaming Application by using HTTP Handler
 *
 * @author Ranga Reddy
 * @created 8-Jun-2023
 * @version 1.0
 */

object StopByHttpHandler {

  // Create a logger instance for logging messages
  @transient private lazy val logger: Logger = Logger.getLogger(getClass.getSimpleName.replace("$", ""))

  /**
   * Responsible for starting the guardian jetty service
   *
   * @param port    - The port number exposed externally
   * @param ssc     - Streaming Context
   * @param appName - The name of the Spark application
   */
  def httpServer(port: Int, ssc: StreamingContext, appName: String): Unit = {
    // Log a debug message indicating the start of the HTTP server
    logger.debug("Starting the HTTP Server with the port " + port)

    // Create a new Jetty server instance
    val server = new Server(port)

    // Create a new context handler
    val context = new ContextHandler()

    // Set the context path for the HTTP server
    val contextPath = "/shutdown/" + appName
    logger.info(s"Server Context Path $contextPath")
    context.setContextPath(contextPath)

    // Set the handler for the context to a custom handler for stopping the StreamingContext
    context.setHandler(new StopStreamingContextHandler(ssc))

    // Set the handler for the server
    server.setHandler(context)

    // Start the Jetty server
    server.start()

    // Log a message indicating the successful start of the HTTP server
    logger.info("HttpServer started successfully")
  }

  /**
   * Responsible for accepting http requests to gracefully shutdown the streaming application
   *
   * @param ssc - Streaming Context
   */
  private class StopStreamingContextHandler(ssc: StreamingContext) extends AbstractHandler {
    override def handle(target: String, baseRequest: Request, request: HttpServletRequest,
                        response: HttpServletResponse): Unit = {
      // Log the target of the request
      logger.info("Serving target: " + target)

      // Log a message indicating the graceful stopping of the Spark Streaming application
      logger.info("Gracefully stopping Spark Streaming Application")

      // Stop the StreamingContext, including the underlying SparkContext, gracefully
      ssc.stop(stopSparkContext = true, stopGracefully = true)

      // Log a message indicating the successful stopping of the application
      logger.info("Spark Streaming Application successfully stopped")

      // Set the content type and status of the HTTP response
      response.setContentType("text/html; charset=utf-8")
      response.setStatus(HttpServletResponse.SC_OK)

      // Write a response message to the HTTP response
      response.getWriter.println("The Spark Streaming application has been successfully stopped.")

      // Mark the base request as handled
      baseRequest.setHandled(true)
    }
  }
}
