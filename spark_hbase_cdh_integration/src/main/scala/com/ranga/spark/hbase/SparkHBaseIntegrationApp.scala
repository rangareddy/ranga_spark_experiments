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

  val conf = HBaseConfiguration.create()
  conf.addResource(new Path("/etc/hbase/conf/hbase-site.xml"))
  new HBaseContext(spark.sparkContext, conf)

  case class Employee(id:Long, name: String, age: Integer, salary: Float)

  import spark.implicits._
  
  var employeeDS = Seq(
    Employee(1L, "Ranga Reddy", 32, 80000.5f),
    Employee(2L, "Nishanth Reddy", 3, 180000.5f),
    Employee(3L, "Raja Sekhar Reddy", 59, 280000.5f)
  ).toDS()

  val columnMapping = "id Long :key, name STRING e:name, age Integer e:age, salary FLOAT e:salary"
  val format = "org.apache.hadoop.hbase.spark"
  val tableName = "employees"

  // write the data to hbase table
  employeeDS.write.format(format).option("hbase.columns.mapping", columnMapping).option("hbase.table", tableName).save()

  // read the data from hbase table
  val df = spark.read.format(format).option("hbase.columns.mapping", columnMapping).option("hbase.table", tableName).load()
  df.show(truncate=false)

  spark.close()
}
