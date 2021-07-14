package com.ranga.spark.hwc

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.SparkConf
import org.apache.log4j.Logger
import com.hortonworks.hwc.HiveWarehouseSession
import com.hortonworks.spark.sql.hive.llap.HiveWarehouseSessionImpl

/**
 * @author Ranga Reddy
 * Version: 1.0
 * Created : 07/14/2021
 */

object SparkHwcIntegrationApp extends Serializable {

    @transient lazy val logger: Logger = Logger.getLogger(getClass.getName)

    def main(args: Array[String]): Unit = {
        

        val appName = "Spark Hwc Integration"
        
        // Creating the SparkConf object
        val sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[2]")
    
        // Creating the SparkSession object
        val spark: SparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()
        logger.info("SparkSession created successfully")
            
        val hive = HiveWarehouseSession.session(spark).build()
        logger.info("HiveWarehouseSession created successfully")

        val database_name = "hwc_db";
        val table_name = "employees";
        val database_table_name = database_name +"."+table_name;

        // Create a Database
        hive.createDatabase(database_name, true)

        // Display all databases
        hive.showDatabases().show(false)

        // Use database
        hive.setDatabase(database_name)

        // Display all tables
        hive.showTables().show(false)

        // Drop a table
        hive.dropTable(database_table_name, true, true)

        // Create a Table
        hive.createTable(table_name).ifNotExists()
            .column("id", "bigint")           
            .column("name", "string")           
            .column("age", "smallint")           
            .column("salary", "float").create()
        
        // Creating a dataset
        val employeeDF = getEmployeeDS(spark)
        employeeDF.printSchema()
        employeeDF.show(false)
        
        // Save the data
        saveEmployeeData(employeeDF, database_table_name)
        
        // Select the data
        val empDF = getEmployeeData(hive, database_table_name)
        empDF.printSchema()
        empDF.show(truncate=false)


        logger.info("<Spark Hwc Integration> successfully finished")

        // Close the SparkSession
        spark.close()
        logger.info("SparkSession closed successfully")
    }

    def getEmployeeDS (spark: SparkSession) : Dataset[Employee] = {
        import spark.implicits._
        Seq(
            Employee(1L, "Ranga Reddy", 32, 80000.5f),
            Employee(2L, "Nishanth Reddy", 3, 180000.5f),
            Employee(3L, "Raja Sekhar Reddy", 59, 280000.5f),
            Employee(4L, "Manoj Reddy", 15, 8000.5f),
            Employee(5L, "Vasundra Reddy", 55, 580000.5f)
        ).toDS()
    }

    def saveEmployeeData(employeeDF: Dataset[Employee], tableName: String): Unit = {
        employeeDF.write.format("com.hortonworks.spark.sql.hive.llap.HiveWarehouseConnector").
          mode("append").option("table", tableName).save();
    }

    def getEmployeeData(hive: HiveWarehouseSessionImpl, tableName: String) : Dataset[Row]  = {
        hive.executeQuery("SELECT * FROM "+tableName)
    }
}