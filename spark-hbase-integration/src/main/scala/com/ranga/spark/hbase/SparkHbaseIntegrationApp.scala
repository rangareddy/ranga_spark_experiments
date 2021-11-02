package com.ranga.spark.hbase

import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf
import org.apache.log4j.Logger

/**
 * @author Ranga Reddy
 * Version: 1.0
 * Created : 10/18/2021
 */

object SparkHbaseIntegrationApp extends Serializable {

    @transient lazy val logger: Logger = Logger.getLogger(getClass.getName)

    def main(args: Array[String]): Unit = {
        
        val appName = "Spark Hbase Integration"
        
        // Creating the SparkConf object
        val sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[2]")

        // Creating the SparkSession object
        val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
        logger.info("SparkSession created successfully")

        import spark.implicits._
        val employeeDS = Seq(
          Employee(1L, "Ranga Reddy", 32, 80000.5f),
          Employee(2L, "Nishanth Reddy", 3, 180000.5f),
          Employee(3L, "Raja Sekhar Reddy", 59, 280000.5f),
          Employee(4L, "Manoj Reddy", 15, 8000.5f),
          Employee(5L, "Vasundra Reddy", 55, 580000.5f)
        ).toDS()
    
        val columnMapping = "id Long :key, name STRING e:name, age Integer e:age, salary FLOAT e:salary"
        val format = "org.apache.hadoop.hbase.spark"
        val tableName = "employees"
    
        val options = Map( "hbase.columns.mapping" -> columnMapping, "hbase.spark.use.hbasecontext" -> "false", "hbase.table" -> tableName)
    
        // write the data to hbase table
        employeeDS.write.format(format).
          options(options).
          save()
    
        // read the data from hbase table
        val df = spark.read.format(format).
          options(options).
          load()
    
        // display the data
        df.show(truncate=false)

        logger.info("<Spark Hbase Integration> successfully finished")

        // Close the SparkSession
        spark.close()
        logger.info("SparkSession closed successfully")
    }

    
}