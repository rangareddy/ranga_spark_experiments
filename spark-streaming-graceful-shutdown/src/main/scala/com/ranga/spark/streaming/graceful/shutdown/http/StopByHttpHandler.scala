package com.ranga.spark.streaming.graceful.shutdown.http

import org.apache.log4j.Logger
import org.apache.spark.streaming._
import org.spark_project.jetty.server.handler.{AbstractHandler, ContextHandler}
import org.spark_project.jetty.server.{Request, Server}

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

object StopByHttpHandler {

  @transient private lazy val logger: Logger = Logger.getLogger(getClass.getSimpleName)

  /**
   * Responsible for starting the guardian jetty service
   *
   * @param port - The port number exposed externally
   * @param ssc  - Streaming Context
   */
  def httpServer(port: Int, ssc: StreamingContext, appName: String): Unit = {
    logger.debug("Starting the HTTP Server with the port " + port)
    val server = new Server(port)
    val context = new ContextHandler()
    val contextPath = "/shutdown/" + appName
    logger.info(s"Server Context Path $contextPath")
    context.setContextPath(contextPath)
    context.setHandler(new StopStreamingContextHandler(ssc))
    server.setHandler(context)
    server.start()
    logger.info("HttpServer started successfully")
  }

  /**
   * Responsible for accepting http requests to gracefully shutdown the streaming application
   *
   * @param ssc - Streaming Context
   */
  private class StopStreamingContextHandler(ssc: StreamingContext) extends AbstractHandler {
    override def handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {
      logger.info("Serving target: " + target)
      logger.info("Gracefully stopping Spark Streaming Application")
      ssc.stop(stopSparkContext = true, stopGracefully = true)
      logger.info("Spark Streaming Application successfully stopped")
      response.setContentType("text/html; charset=utf-8")
      response.setStatus(HttpServletResponse.SC_OK)
      response.getWriter.println("Spark Streaming Application successfully stopped")
      baseRequest.setHandled(true)
    }
  }
}