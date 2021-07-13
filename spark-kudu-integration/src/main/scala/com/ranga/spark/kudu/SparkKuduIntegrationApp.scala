package com.ranga.spark.kudu

import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf
import org.apache.log4j.Logger
import collection.JavaConverters._
import org.apache.kudu.client._
import org.apache.kudu.spark.kudu.KuduContext

/**
 * @author Ranga Reddy
 * Version: 1.0
 * Created : 07/13/2021
 */

object SparkKuduIntegrationApp extends Serializable {

    @transient lazy val logger: Logger = Logger.getLogger(getClass.getName)

    def main(args: Array[String]): Unit = {
        
		if(args.length > 1 ) {
				System.out.println("Usage : SparkKuduIntegrationApp <KUDU_MASTER>");
				System.exit(0);
		}

        val appName = "Spark Kudu Integration"
        
        // Creating the SparkConf object
        val sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[2]")
    
        // Creating the SparkSession object
        val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
        logger.info("SparkSession created successfully")

        import spark.implicits._
        val employeeDF = Seq(
          Employee(1L, "Ranga Reddy", 32, 80000.5f),
          Employee(2L, "Nishanth Reddy", 3, 180000.5f),
          Employee(3L, "Raja Sekhar Reddy", 59, 280000.5f),
          Employee(4L, "Manoj Reddy", 15, 8000.5f),
          Employee(5L, "Vasundra Reddy", 55, 580000.5f)
        ).toDF()

        val master = args(0)
        val kuduMasters = if(!master.contains(":7051")) s"${master}:7051" else master

        // If table is created from Impala then table name prefix with "impala::"
        val kuduTableName = "default.employees"
        // Use KuduContext to create, delete, or write to Kudu tables
        val kuduContext = new KuduContext(kuduMasters, spark.sparkContext)

        // Check for the existence of a Kudu table
        val isTableExists = kuduContext.tableExists(kuduTableName)

        if(!isTableExists) {
            // Create a new Kudu table from a DataFrame schema
            kuduContext.createTable(kuduTableName, employeeDF.schema, Seq("id"),
                new CreateTableOptions()
                  .setNumReplicas(1)
                  .addHashPartitions(List("id").asJava, 3))
        }

        // Insert data
        kuduContext.insertRows(employeeDF, kuduTableName)

        // Read a table from Kudu
        val empDF = spark.read
          .options(Map("kudu.master" -> kuduMasters, "kudu.table" -> kuduTableName))
          .format("kudu").load

        empDF.printSchema()
        empDF.show()

        // Delete data
        kuduContext.deleteRows(employeeDF, kuduTableName)

        // Upsert data
        kuduContext.upsertRows(employeeDF, kuduTableName)

        // Update data
        val updateEmployeeDF = employeeDF.select($"age", ($"age" + 1).as("age_val"))
        kuduContext.updateRows(updateEmployeeDF, kuduTableName)

        // Delete a Kudu table
        kuduContext.deleteTable(kuduTableName)

        logger.info("<Spark Kudu Integration> successfully finished")

        // Close the SparkSession
        spark.close()
        logger.info("SparkSession closed successfully")
    }

    
}