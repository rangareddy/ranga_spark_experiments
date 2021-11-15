package com.ranga.spark.delta.lake

import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.SparkConf
import org.apache.log4j.Logger
import org.apache.commons.io.FileUtils
import java.io.File


/**
 * @author Ranga Reddy
 * Version: 1.0
 * Created : 10/18/2021
 */

object SparkDeltaLakeIntegrationApp extends Serializable {

    @transient lazy val logger: Logger = Logger.getLogger(getClass.getName)

    def main(args: Array[String]): Unit = {
        
        val appName = "Spark Delta Lake Integration"
        
        // Creating the SparkConf object
        val sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[2]")
        sparkConf.set("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
        sparkConf.set("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")

        // Creating the SparkSession object
        val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
        logger.info("SparkSession created successfully")

        val deltaPath = "/tmp/delta-table"
        val deltaFile = new File(deltaPath)
        if (deltaFile.exists()) FileUtils.deleteDirectory(deltaFile)

        val employeeDS = getEmployeeDS(spark)
        employeeDS.show()

        // Create a table
        logger.info("Creating a table")
        employeeDS.write.format("delta").save(deltaPath)

        // Read table
        logger.info("Reading the table")
        val employeeDF = spark.read.format("delta").load(deltaPath)
        employeeDF.show()

        logger.info("<Spark Delta Lake Integration> successfully finished")

        // Close the SparkSession
        spark.close()
        logger.info("SparkSession closed successfully")
    }

    def getEmployeeDS(spark: SparkSession): Dataset[Employee] = {
        import spark.implicits._
        Seq(
            Employee(1L, "Ranga Reddy", 32, 80000.5f),
            Employee(2L, "Nishanth Reddy", 3, 180000.5f),
            Employee(3L, "Raja Sekhar Reddy", 59, 280000.5f),
            Employee(4L, "Manoj Reddy", 15, 8000.5f),
            Employee(5L, "Vasundra Reddy", 55, 580000.5f)
        ).toDS()
    }
}