package com.ranga.spark.streaming.secure

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.avro.to_avro
import org.apache.spark.sql.functions.{col, from_json, struct}
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}

object SparkSecureKafkaStructuredStreamingJsonApp extends App with Serializable {

  if (args.length < 3) {
    println("Usage: SparkSecureKafkaStructuredStreamingJsonApp <bootstrap_servers> <src_topic_name> <dest_topic_name>")
    println("Example: SparkSecureKafkaStructuredStreamingJsonApp localhost:9092 json_topic avro_topic")
    System.exit(0)
  }

  val conf = new SparkConf().setIfMissing("spark.master", "local[*]").setAppName("Spark Secure Kafka Structured Streaming Json App")
  val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")

  val bootstrapServers = args(0)
  val jsonTopicName = args(1)
  val avroTopicName = args(2)
  val startingOffsets = if (args.length > 3) {
    args(3)
  } else {
    "earliest"
  }
  val checkpointLocation = "/tmp/checkpoint/spark_kafka_structured_streaming/"

  val df = spark.readStream
    .format("kafka")
    .option("kafka.security.protocol", "SASL_PLAINTEXT")
    .option("kafka.bootstrap.servers", bootstrapServers)
    .option("subscribe", jsonTopicName)
    .option("startingOffsets", startingOffsets)
    .load()

  df.printSchema()

  val employeeJsonSchema = new StructType()
    .add("id", IntegerType)
    .add("name", StringType)
    .add("age", IntegerType)
    .add("salary", IntegerType)

  val employeeDF = df.selectExpr("CAST(value AS STRING)")
    .select(from_json(col("value"), employeeJsonSchema).as("employee"))

  employeeDF.printSchema()

  employeeDF.select(to_avro(struct("employee.*")) as "value")
    .writeStream
    .format("kafka")
    .outputMode("append")
    .option("kafka.security.protocol", "SASL_PLAINTEXT")
    .option("kafka.bootstrap.servers", bootstrapServers)
    .option("topic", avroTopicName)
    .option("checkpointLocation", checkpointLocation)
    .start()
    .awaitTermination()
}
