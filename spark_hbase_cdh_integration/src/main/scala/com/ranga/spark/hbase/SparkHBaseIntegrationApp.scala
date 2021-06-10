package com.ranga.spark.hbase

import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf
import org.apache.hadoop.hbase.spark.HBaseContext
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.fs.Path

object SparkHBaseIntegrationApp extends App with Serializable {

  println("Creating the SparkSession")
  val sparkConf = new SparkConf().setAppName("Spark HBase Integration").setIfMissing("spark.master", "local[4]")
  val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
  println("SparkSession created")

  // Create the HBaseContext
  val conf = HBaseConfiguration.create()
  conf.addResource(new Path("/etc/hbase/conf/hbase-site.xml"))
  new HBaseContext(spark.sparkContext, conf)

  val columnMapping = "id STRING :key, name STRING per:name, age STRING per:age, designation STRING prof:designation, salary STRING prof:salary"
  val tableName = "employee"

  // Read the data from hbase table
  val employeeDf = spark.read.format("org.apache.hadoop.hbase.spark").option("hbase.columns.mapping", columnMapping).option("hbase.table", tableName).load()
  employeeDf.printSchema()
  employeeDf.show(truncate=false)

  // Close the SparkSession
  spark.close()
  println("SparkSession closed")
}
