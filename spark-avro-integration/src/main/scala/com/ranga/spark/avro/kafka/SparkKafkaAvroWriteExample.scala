package com.ranga.spark.avro.kafka

import com.ranga.spark.avro.bean.Employee
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.avro.to_avro
import org.apache.spark.sql.functions.{col, struct}

/* Ranga Reddy created on 20/06/20 */
object SparkKafkaAvroWriteExample extends App {

    if (args.length < 2) {
        System.err.println("Usage: SparkKafkaAvroWriteExample <bootstrap.servers> <topic.names>")
        System.exit(0)
    }

    val appName = "Spark Kafka Avro Write Example"
    var Array(bootstrapServers, topicName) = args.take(2)

    val conf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[*]")
    val spark = SparkSession.builder().config(conf).getOrCreate();

    val employeeList = List(Employee(1, "Ranga", 30, 10000.2f),
        Employee(2, "Vinod", 25, 1000.67f),
        Employee(3, "Nishanth", 2, 500000.76f),
        Employee(4, "Manoj", 14, 25000.45f),
        Employee(5, "Yashu", 10, 1600.98f),
        Employee(6, "Raja", 50, 50000.63f)
    )

    import spark.sqlContext.implicits._
    val employeeDF = spark.createDataset(employeeList)

    employeeDF.select(to_avro(struct("data.*")) as "value")
        .writeStream
        .format("kafka")
        .outputMode("append")
        .option("kafka.bootstrap.servers", bootstrapServers)
        .option("topic", topicName)
        .start()
        .awaitTermination()
}
