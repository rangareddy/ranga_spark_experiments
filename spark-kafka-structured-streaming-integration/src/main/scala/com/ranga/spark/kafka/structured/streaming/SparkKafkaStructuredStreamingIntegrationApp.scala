package com.ranga.spark.kafka.structured.streaming

import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf

object SparkKafkaStructuredStreamingIntegrationApp extends App with Serializable {

  val appName = "SparkKafkaStructuredStreamingIntegrationApp Example"

  // Creating the SparkConf object
  val sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[*]")

  // Creating the SparkSession object
  val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
  println("SparkSession Created successfully")

  val dataset = spark.range(1, 1000)
  dataset.printSchema()
  dataset.show()

  // Close the SparkSession
  spark.close()
  println("SparkSession closed successfully")
}