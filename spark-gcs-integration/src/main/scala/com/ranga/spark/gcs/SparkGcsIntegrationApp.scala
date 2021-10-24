package com.ranga.spark.gcs

import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.SparkConf
import org.apache.log4j.Logger

/**
 * @author Ranga Reddy
 * Version: 1.0
 * Created : 10/18/2021
 */

object SparkGcsIntegrationApp extends Serializable {

    @transient lazy val logger: Logger = Logger.getLogger(getClass.getName)

    def main(args: Array[String]): Unit = {
        
        if(args.length > 5 ) {
            System.err.println("Usage : SparkGcsIntegrationApp <PROJECT_ID> <BUCKET_NAME> <PRIVATE_KEY> <PRIVATE_KEY_ID> <CLIENT_EMAIL>");
            System.exit(0);
        }

        val appName = "Spark Gcs Integration"
        
        // Creating the SparkConf object
        val sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[2]")

        // Creating the SparkSession object
        val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
        logger.info("SparkSession created successfully")

        
        val projectId = args(0)
        val bucketName = args(1)
        val privateKey = args(2)
        val privateKeyId = args(3)
        val clientEmail = args(4)

        // GCS settings
        val conf = spark.sparkContext.hadoopConfiguration
        conf.set("fs.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem")
        conf.set("fs.AbstractFileSystem.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFS")
        conf.set("fs.gs.auth.service.account.enable", "true")
        conf.set("fs.gs.project.id", projectId)
        conf.set("fs.gs.auth.service.account.private.key", privateKey)
        conf.set("fs.gs.auth.service.account.private.key.id", privateKeyId)
        conf.set("fs.gs.auth.service.account.email", clientEmail)

        import spark.implicits._
        val employeeDF = Seq(
          Employee(1L, "Ranga Reddy", 32, 80000.5f),
          Employee(2L, "Nishanth Reddy", 3, 180000.5f),
          Employee(3L, "Raja Sekhar Reddy", 59, 280000.5f),
          Employee(4L, "Manoj Reddy", 15, 8000.5f),
          Employee(5L, "Vasundra Reddy", 55, 580000.5f)
        ).toDF()
        
        employeeDF.printSchema()
        employeeDF.show()

        // Define the gcs destination path
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

        logger.info("<Spark Gcs Integration> successfully finished")

        // Close the SparkSession
        spark.close()
        logger.info("SparkSession closed successfully")
    }

    
}