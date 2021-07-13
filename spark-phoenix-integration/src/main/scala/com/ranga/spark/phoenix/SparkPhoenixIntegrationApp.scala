package com.ranga.spark.phoenix

import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.SparkConf
import org.apache.log4j.Logger

/**
 * @author Ranga Reddy
 * Version: 1.0
 * Created : 07/13/2021
 */

object SparkPhoenixIntegrationApp extends Serializable {

    @transient lazy val logger: Logger = Logger.getLogger(getClass.getName)

    def main(args: Array[String]): Unit = {
        
		if(args.length > 2 ) {
				System.out.println("Usage : SparkPhoenixIntegrationApp <PHOENIX_SERVER_URL> <TABLE_NAME>");
				System.exit(0);
		}

        val appName = "Spark Phoenix Integration"
        
        // Creating the SparkConf object
        val sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[2]")
    
        // Creating the SparkSession object
        val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
        logger.info("SparkSession created successfully")

        val sqlContext = spark.sqlContext

        var phoenixServerUrl = args(0) // val zkUrl="phoenix-server:2181"
        val tableName = args(1)
        
        if(!phoenixServerUrl.contains(":")) {
            phoenixServerUrl = phoenixServerUrl +":2181"
        }

        val inputDF = sqlContext.load(
          "org.apache.phoenix.spark",
          Map("table" -> tableName, "zkUrl" -> phoenixServerUrl)
        )
      
        inputDF.show(true)

        logger.info("<Spark Phoenix Integration> successfully finished")

        // Close the SparkSession
        spark.close()
        logger.info("SparkSession closed successfully")
    }

    
}