package com.ranga.spark.s3

import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.SparkConf
import org.apache.log4j.Logger

/**
 * @author Ranga Reddy
 * Version: 1.0
 * Created : 07/14/2021
 */

object SparkS3IntegrationApp extends Serializable {

    @transient lazy val logger: Logger = Logger.getLogger(getClass.getName)

    def main(args: Array[String]): Unit = {
        
        if(args.length > 3 ) {
            System.err.println("Usage : SparkS3IntegrationApp <AWS_ACCESS_KEY_ID> <AWS_SECRET_ACCESS_KEY> <BUCKET_NAME>");
            System.exit(0);
        }

        val appName = "Spark S3 Integration"
        
        // Creating the SparkConf object
        val sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[2]")
    
        // Creating the SparkSession object
        val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
        logger.info("SparkSession created successfully")

        
        val awsAccessKey = args(0)
        val awsSecretKey = args(1)
        val bucketName = args(2)

        // S3 settings
        val conf = spark.sparkContext.hadoopConfiguration
        conf.set("spark.hadoop.fs.s3a.access.key", awsAccessKey)
        conf.set("spark.hadoop.fs.s3a.secret.key", awsSecretKey)
        conf.set("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
        conf.set("spark.speculation", "false")
        conf.set("spark.hadoop.mapreduce.fileoutputcommitter.algorithm.version", "2")
        conf.set("spark.hadoop.mapreduce.fileoutputcommitter.cleanup-failures.ignored", "true")
        conf.set("fs.s3a.experimental.input.fadvise", "random")

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


        logger.info("<Spark S3 Integration> successfully finished")

        // Close the SparkSession
        spark.close()
        logger.info("SparkSession closed successfully")
    }

    
}