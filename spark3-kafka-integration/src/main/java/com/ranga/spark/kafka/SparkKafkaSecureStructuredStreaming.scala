
package com.ranga.spark.kafka

import org.apache.spark.sql.SparkSession

object SparkKafkaSecureStructuredStreaming extends App {

    if (args.length < 3) {

    	System.err.println(s"""
                            |Usage: SparkKafkaSecureStructuredStreaming <bootstrap-servers> <protocol> <topics>
                            |  <bootstrap-servers> The Kafka "bootstrap.servers" configuration. A comma-separated list of host:port.
                            |  <protocol> Protocol used to communicate with brokers. Valid values are: 'PLAINTEXT', 'SSL', 'SASL_PLAINTEXT', 'SASL_SSL'.
                            |  <topics> The topic list to subscribe. A comma-separated list of topics.
      	""".stripMargin)

      	System.out.println(s"""
                            |Example: SparkKafkaSecureStructuredStreaming localhost:9092 SASL_PLAINTEXT KafkaWordCount
      	""".stripMargin)

    	System.exit(1)
	}

	val Array(bootStrapServers, protocol, topicNames) = args

	val spark = SparkSession
      .builder
      .appName("Spark Kafka Secure Structured Streaming")
      .getOrCreate()


	val inputDF = spark
	  .readStream
	  .format("kafka")
	  .option("kafka.bootstrap.servers", bootStrapServers)
	  .option("subscribe", topicName)
	  .option("kafka.security.protocol",protocol)
	  .option("kafka.sasl.kerberos.service.name", "kafka")
	  .option("startingoffsets", "earliest")
	  .load()
	  .selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
	  .as[(String, String)]

	// Start running the query that prints the running counts to the console
	val query = inputDF.writeStream
	  .outputMode("complete")
	  .format("console")
	  .start()

	query.awaitTermination()

}
