package com.ranga.spark.avro.kafka

import java.nio.file.{Files, Paths}

import com.ranga.spark.avro.bean.Employee
import com.ranga.spark.avro.kafka.SparkKafkaAvroWriteExample.{bootstrapServers, spark, topicName}
import org.apache.avro.SchemaBuilder
import org.apache.spark.SparkConf
import org.apache.spark.sql.{Encoders, SparkSession}
import org.apache.spark.sql.avro.{from_avro, to_avro}
import org.apache.spark.sql.functions.col

/* Ranga Reddy created on 20/06/20 */
object SparkKafkaAvroReadExample extends App {

    if (args.length < 2) {
        System.err.println("Usage: SparkKafkaAvroReadExample <bootstrap.servers> <topic.names>")
        System.exit(0)
    }

    val appName = "Spark Kafka Avro Read Example"
    var Array(bootstrapServers, topicName) = args.take(2)

    val conf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[*]")
    val spark = SparkSession.builder().config(conf).getOrCreate();

    val kafkaInputStream = spark
        .readStream
        .format("kafka")
        .option("kafka.bootstrap.servers", bootstrapServers)
        .option("subscribe", topicName)
        .option("startingOffsets", "earliest")
        .load()

    kafkaInputStream.printSchema()
    kafkaInputStream.show(5)

    val employeeSchema = new String(Files.readAllBytes(Paths.get(".src/main/resources/employee.avsc")))

    val employeeDF = kafkaInputStream.select(
        from_avro(col("key"), SchemaBuilder.builder().stringType().toString).as("key"),
        from_avro(col("value"), employeeSchema).as("value"))

    employeeDF.printSchema()

    val consoleWriteStream = employeeDF.writeStream.
        format("console").
        option("truncate","false").
        outputMode("append")

    consoleWriteStream.start().awaitTermination()

}
