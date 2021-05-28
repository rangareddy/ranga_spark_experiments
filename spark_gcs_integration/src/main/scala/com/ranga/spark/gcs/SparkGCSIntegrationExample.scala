package com.ranga.spark.gcs

import org.apache.spark.SparkConf
import org.apache.spark.sql._

object SparkGCSIntegrationExample {

  case class Employee(id: Long, name: String, age: Int, salary: Double)

  def main(args: Array[String]): Unit = {

    if (args.length < 5) {
      System.err.println("Usage   : SparkGCSIntegrationExample <PROJECT_ID> <BUCKET_NAME> <PRIVATE_KEY> <PRIVATE_KEY_ID> <CLIENT_EMAIL>")
      System.out.println("Example : SparkGCSIntegrationExample ranga-gcp-spark-project ranga-spark-gcp-bkt ranga_private_key ranga_private_key_id rangareddy@project.iam.gserviceaccount.com")
      System.exit(0)
    }

    val projectId = args(0)
    val bucketName = args(1)
    val privateKey = args(2)
    val privateKeyId = args(3)
    val clientEmail = args(4)

    // Creating the SparkConf object
    val sparkConf = new SparkConf().setAppName("Spark GCS Integration Example").
      setIfMissing("spark.master", "local")

    // Creating the SparkSession object
    val spark = SparkSession.builder.config(sparkConf).getOrCreate
    println("SparkSession Created successfully")

    // GCS settings
    val conf = spark.sparkContext.hadoopConfiguration
    conf.set("fs.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem")
    conf.set("fs.AbstractFileSystem.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFS")
    conf.set("fs.gs.auth.service.account.enable", "true")
    conf.set("fs.gs.project.id", projectId)
    conf.set("fs.gs.auth.service.account.private.key", privateKey)
    conf.set("fs.gs.auth.service.account.private.key.id", privateKeyId)
    conf.set("fs.gs.auth.service.account.email", clientEmail)

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
    val gcs_dest_path = "gs://" + bucketName + "/employees"

    // Write the data as Orc
    val employeeOrcPath = gcs_dest_path + "/employee_orc"
    employeeDF.write.mode("overwrite").format("orc").save(employeeOrcPath)

    // Read the employee orc data
    val employeeOrcData = spark.read.format("orc").load(employeeOrcPath)
    employeeOrcData.printSchema()
    employeeOrcData.show()

    // Write the data as Parquet
    val employeeParquetPath = gcs_dest_path + "/employee_parquet"
    employeeOrcData.write.mode("overwrite").format("parquet").save(employeeParquetPath)

    // Close the SparkSession
    spark.close()

    System.out.println("SparkSession stopped")
  }
}