package com.ranga.spark.streaming.shutdown.util.jdbc

import org.apache.log4j.Logger
import org.apache.spark.streaming.StreamingContext

import java.sql._

/**
 * This class can be used to stop a Spark Streaming Context gracefully by checking for a marker record in an RDBMS.
 *
 * @author Ranga Reddy
 */
class StopByMarkerRDBMS(val driver: String, val url: String, val username: String,
                        val password: String) {

  // Create a logger instance for logging messages
  @transient private lazy val logger: Logger = Logger.getLogger(getClass.getSimpleName.replace("$", ""))

  private var connection: Connection = _

  /**
   * This method checks for a marker record in the RDBMS. If the marker record exists, the Spark Streaming Context is stopped gracefully.
   *
   * @param ssc     The Spark Streaming Context
   * @param appName The name of the Spark Streaming application
   */
   def stopByMarkerRDBMS(ssc: StreamingContext, appName: String): Unit = {
    var isStop = false
    while (!isStop) {
      // If the marker record exists, stop the Spark Streaming Context
      if (!isStop && checkMarkerRecordExists(appName)) {
        logger.info("Marker record exists, stopping the Spark Streaming Context gracefully ...")
        // Stop the streaming context if it has not already terminated
        ssc.stop(stopSparkContext = true, stopGracefully = true)
        updateMarkerRecord(appName)
        logger.info("Spark Streaming Context is Stopped ...")
        isStop = true
        close()
      }
    }
  }

  /**
   * Closes the JDBC connection.
   */
  private def close(): Unit = {
    if (connection != null) {
      connection.close()
    }
  }

  /**
   * Checks if a marker record exists in the RDBMS.
   *
   * @param appName The name of the Spark Streaming application
   * @return True if the marker record exists, false otherwise
   */
  private def checkMarkerRecordExists(appName: String): Boolean = {
    var statement: PreparedStatement = null
    var isMarkerRecordExists = false
    try {
      val sqlQuery: String = s"SELECT * FROM MARKER_DATA WHERE NAME=? AND UPPER(status) != 'RUNNING'"
      statement = getConnection.prepareStatement(sqlQuery)
      statement.setString(1, appName)
      isMarkerRecordExists = statement.executeQuery().next()
    } finally {
      if (statement != null) {
        statement.close()
      }
    }
    isMarkerRecordExists
  }

  /**
   * Updates the marker record in the RDBMS.
   *
   * @param appName The name of the Spark Streaming application
   */

  private def updateMarkerRecord(appName: String): Unit = {
    updateMarkerRecordByStatus(appName, "RUNNING")
  }

  def updateMarkerRecordByStatus(appName: String, status: String): Unit = {
    var statement: PreparedStatement = null
    try {
      val sqlQuery = "UPDATE MARKER_DATA SET status = ? WHERE name = ?"
      statement = getConnection.prepareStatement(sqlQuery)
      statement.setString(1, status)
      statement.setString(2, appName)
      statement.executeUpdate()
    } finally {
      if (statement != null) {
        statement.close()
      }
    }
  }

  /**
   * Gets a connection to the RDBMS.
   *
   * @return The JDBC connection
   */
  private def getConnection: Connection = {
    if (connection == null) {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)
    }
    connection
  }
}
