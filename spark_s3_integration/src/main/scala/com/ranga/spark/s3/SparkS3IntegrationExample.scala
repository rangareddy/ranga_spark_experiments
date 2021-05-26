package com.ranga.spark.s3

import org.apache.spark.SparkConf
import org.apache.spark.sql._

object SparkS3IntegrationExample {

  case class Employee(id: Long, name: String, age: Int, salary: Double)

  def main(args: Array[String]): Unit = {

    if (args.length < 3) {
      System.err.println("Usage   : SparkS3IntegrationExample <AWS_ACCESS_KEY_ID> <AWS_SECRET_ACCESS_KEY> <BUCKET_NAME>")
      System.out.println("Example : SparkS3IntegrationExample ranga_aws_access_key ranga_aws_secret_access_key ranga-spark-s3-bkt")
      System.exit(0)
    }

    val awsAccessKey = args(0)
    val awsSecretKey = args(1)
    val bucketName = args(2)

    // Creating the SparkConf object
    val sparkConf = new SparkConf().setAppName("Spark S3 Integration Example").
      set("spark.hadoop.fs.s3a.access.key", awsAccessKey).
      set("spark.hadoop.fs.s3a.secret.key", awsSecretKey).
      set("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem").
      set("spark.speculation", "false").
      set("spark.hadoop.mapreduce.fileoutputcommitter.algorithm.version", "2").
      set("spark.hadoop.mapreduce.fileoutputcommitter.cleanup-failures.ignored", "true").
      set("fs.s3a.experimental.input.fadvise", "random").
      setIfMissing("spark.master", "local")

    // Creating the SparkSession object
    val spark = SparkSession.builder.config(sparkConf).getOrCreate
    println("SparkSession Created successfully")

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
    val s3_dest_path = "s3a://" + bucketName + "/employees"

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

    System.out.println("SparkSession stopped")
  }
}