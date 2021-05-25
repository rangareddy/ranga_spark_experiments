package com.ranga.spark.s3

import org.apache.spark.SparkConf
import org.apache.spark.sql._

object SparkS3IntegrationExample {

  private val AWS_BUCKET_NAME = "YOUR_AWS_BUCKET_NAME"
  private val AWS_ACCESS_KEY_ID = "YOUR_AWS_ACCESS_KEY_ID"
  private val AWS_SECRET_ACCESS_KEY = "YOUR_AWS_SECRET_ACCESS_KEY"

  case class Employee(id: Long, name: String, age: Int, salary: Double)

  def main(args: Array[String]): Unit = {

    // Creating the SparkConf object
    val sparkConf = new SparkConf().setAppName("Spark S3 Integration Example").
      set("spark.hadoop.fs.s3a.access.key", AWS_ACCESS_KEY_ID).
      set("spark.hadoop.fs.s3a.secret.key", AWS_SECRET_ACCESS_KEY).
      set("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem").
      set("spark.hadoop.mapreduce.fileoutputcommitter.algorithm.version", "2").
      set("spark.speculation", "false").
      set("spark.hadoop.mapreduce.fileoutputcommitter.cleanup-failures.ignored", "true").
      set("fs.s3a.experimental.input.fadvise", "random").
      setIfMissing("spark.master", "local")

    // Creating the SparkSession object
    val spark = SparkSession.builder.config(sparkConf).getOrCreate
    println("Spark Created successfully")

    val employeeData = Seq(
      Employee(1, "Ranga", 32, 245000.30),
      Employee(2, "Nishanth", 2, 345000.10),
      Employee(3, "Raja", 32, 245000.86),
      Employee(4, "Mani", 14, 45000)
    )

    val employeeDF = spark.createDataFrame(employeeData)
    employeeDF.printSchema()
    employeeDF.show()

    // Define the s3 destination path
    val s3_dest_path = "s3a://" + AWS_BUCKET_NAME + "/employees"

    // Write the data as Orc
    val employeeOrcPath = s3_dest_path + "/employee_orc"
    employeeDF.write.mode("overwrite").format("orc").save(employeeOrcPath)

    // Read the employee orc data
    val employeeOrcData = spark.read.format("orc").load(employeeOrcPath)
    employeeOrcData.printSchema()
    employeeOrcData.show()

    // Write the data as Parquet
    val employeeParquetPath = s3_dest_path + "/employee_parquet"
    employeeOrcData.write.mode("overwrite").format("parquet").save(employeeParquetPath)

    // Close the SparkSession
    spark.close()
  }
}