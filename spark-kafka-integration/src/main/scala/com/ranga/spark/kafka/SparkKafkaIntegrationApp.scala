package com.ranga.spark.kafka

import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf
import org.apache.log4j.Logger

/**
 * @author Ranga Reddy
 * Version: 1.0
 * Created : 07/13/2021
 */

object SparkKafkaIntegrationApp extends Serializable {

    @transient lazy val logger: Logger = Logger.getLogger(getClass.getName)

    def main(args: Array[String]): Unit = {
        
		if(args.length > 4 ) {
				System.out.println("Usage : SparkKafkaIntegrationApp <KAFKA_BOOTSTRAP_SERVERS> <KAFKA_TOPIC_NAMES> <SSL_TRUSTSTORE_LOCATION> <SSL_TRUSTSTORE_PASSWORD>");
				System.exit(0);
		}

        val appName = "Spark Kafka Integration"
        
        // Creating the SparkConf object
        val sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[2]")
    
        // Creating the SparkSession object
        val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
        logger.info("SparkSession created successfully")

        val kafkaBootstrapServers = args(0)
        val inputTopicNames = args(1)
        val sslTruststoreLocation = args(2)
        val sslTruststorePassword = args(3)

        val inputDf = spark.
            readStream.
            format("kafka").
            option("kafka.bootstrap.servers", kafkaBootstrapServers).
            option("subscribe", inputTopicNames).
            option("startingOffsets", "earliest"). 
            option("kafka.security.protocol","SASL_SSL"). 
            option("kafka.ssl.truststore.location", sslTruststoreLocation).
            option("kafka.ssl.truststore.password", sslTruststorePassword).
            load()
        
        inputDf.printSchema()

        val outputDF = inputDf.writeStream.
            format("console").
            outputMode("append").
            option("truncate", "false").
            start()

        outputDF.awaitTermination()

        logger.info("<Spark Kafka Integration> successfully finished")

        
    }

    
}