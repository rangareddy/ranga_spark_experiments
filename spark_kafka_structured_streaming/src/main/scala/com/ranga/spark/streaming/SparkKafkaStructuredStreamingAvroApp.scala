package com.ranga.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.avro.from_avro
import org.apache.spark.sql.functions.col

object SparkKafkaStructuredStreamingAvroApp extends App with Serializable {

  if (args.length < 2) {
    println("Usage: SparkKafkaStructuredStreamingAvroApp <bootstrap_servers> <topic_name> ")
    println("Example: SparkKafkaStructuredStreamingAvroApp localhost:9092 avro_topic ")
    System.exit(0)
  }

  val conf = new SparkConf().setIfMissing("spark.master", "local[*]").setAppName("Spark Kafka Structured Streaming Avro App")
  val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
  val bootstrapServers = args(0)
  val topicName = args(1)
  val startingOffsets = if (args.length > 2) {
    args(2)
  } else {
    "earliest"
  }

  val df = spark.readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", bootstrapServers)
    .option("subscribe", topicName)
    .option("startingOffsets", startingOffsets)
    .load()

  df.printSchema()

  def loadResource(filename: String) = {
    val source = scala.io.Source.fromInputStream(getClass.getClassLoader.getResourceAsStream(filename))
    try source.mkString finally source.close()
  }

  val employeeAvroSchema = loadResource("employee.avsc")
  val employeeDF = df.select(from_avro(col("value"), employeeAvroSchema).as("employee")).select("employee.*")
  employeeDF.printSchema()

  employeeDF.writeStream
    .format("console")
    .outputMode("append")
    .start()
    .awaitTermination()
}