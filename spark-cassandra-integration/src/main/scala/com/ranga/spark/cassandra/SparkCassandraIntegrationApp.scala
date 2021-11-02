package com.ranga.spark.cassandra

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.SparkConf
import org.apache.log4j.Logger

/**
 * @author Ranga Reddy
 * Version: 1.0
 * Created : 10/18/2021
 */

object SparkCassandraIntegrationApp extends Serializable {

    @transient lazy val logger: Logger = Logger.getLogger(getClass.getName)

    def main(args: Array[String]): Unit = {
        
        if(args.length > 1 ) {
            System.err.println("Usage : SparkCassandraIntegrationApp <CASSANDRA_HOST>");
            System.exit(0);
        }

        val appName = "Spark Cassandra Integration"
        
        // Creating the SparkConf object
        val sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[2]")

        val cassandraHost = args(0)
        val cassandraPort = if(args.length > 1) args(1) else "9042"

        sparkConf.set("spark.cassandra.connection.host", cassandraHost)
        sparkConf.set("spark.cassandra.connection.port", cassandraPort)

        // Creating the SparkSession object
        val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
        println("SparkSession created successfully")

        val tableName = "employees"
        val keyspace = "ranga_keyspace"
        val cassandraFormat = "org.apache.spark.sql.cassandra"
        val options = Map( "keyspace" -> keyspace, "table" -> tableName)

        val employeeDF = spark.read.format(cassandraFormat).options(options).load()
        display(employeeDF)

        import spark.implicits._
        var employeeDS = Seq(
          Employee(5L, "Yashwanth", 32, 80000.5f),
          Employee(6L, "Vasundra Reddy", 57, 180000.5f)
        ).toDS()

        employeeDS.write.mode(org.apache.spark.sql.SaveMode.Append).format(cassandraFormat).options(options).save()

        val empDF = spark.read.format(cassandraFormat).options(options).load()
        display(empDF)

        logger.info("<Spark Cassandra Integration> successfully finished")

        // Close the SparkSession
        spark.close()
        logger.info("SparkSession closed successfully")
    }

    
    def display(df: Dataset[Row]) = {
      df.printSchema()
      df.show(truncate=false)
    }
}