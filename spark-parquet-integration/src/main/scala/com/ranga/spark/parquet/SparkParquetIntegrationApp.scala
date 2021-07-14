package com.ranga.spark.parquet

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.SparkConf
import org.apache.log4j.Logger

/**
 * @author Ranga Reddy
 * Version: 1.0
 * Created : 07/14/2021
 */

object SparkParquetIntegrationApp extends Serializable {

    @transient lazy val logger: Logger = Logger.getLogger(getClass.getName)

    def main(args: Array[String]): Unit = {
        

        val appName = "Spark Parquet Integration"
        
        // Creating the SparkConf object
        val sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[2]")
    
        // Creating the SparkSession object
        val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
        logger.info("SparkSession created successfully")

        val employeeDF = getEmployeeDS(spark)
        employeeDF.printSchema()

        // parquet
        val parquetFilePath = "/tmp/parquet_data"
        saveData(employeeDF, "parquet", parquetFilePath)

        val parquetEmployeeDF = loadData(spark, "parquet", parquetFilePath)
        display(parquetEmployeeDF)



        logger.info("<Spark Parquet Integration> successfully finished")

        // Close the SparkSession
        spark.close()
        logger.info("SparkSession closed successfully")
    }

    def getEmployeeDS(spark: SparkSession): Dataset[Row] = {
        import spark.implicits._
        Seq(
            Employee(1L, "Ranga Reddy", 32, 80000.5f),
            Employee(2L, "Nishanth Reddy", 3, 180000.5f),
            Employee(3L, "Raja Sekhar Reddy", 59, 280000.5f),
            Employee(4L, "Manoj Reddy", 15, 8000.5f),
            Employee(5L, "Vasundra Reddy", 55, 580000.5f)
        ).toDF()
    }

    def saveData(df: Dataset[Row], format:String, path: String): Unit = {
        df.coalesce(1).write.format(format).mode("overwrite").save(path)
    }
    def display(df: Dataset[Row]): Unit = {
        df.printSchema()
        df.show()
    }

    def loadData(spark: SparkSession, format:String, path: String) : Dataset[Row] = {
        spark.read.format(format).load(path)
    }
}