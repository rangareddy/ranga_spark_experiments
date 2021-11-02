package com.ranga.spark.hwc;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.*;
import org.apache.log4j.Logger;
import java.io.Serializable;
import java.util.*;
import com.hortonworks.hwc.HiveWarehouseSession;

/**
 * @author Ranga Reddy
 * Version: 1.0
 * Created : 10/18/2021
 */

public class SparkHwcIntegrationJavaApp implements Serializable {

    private static final Logger logger = Logger.getLogger(SparkHwcIntegrationJavaApp.class.getName());

    public static void main(String[] args) {

        String appName = "Spark Hwc Integration";
        
        // Creating the SparkConf object
        SparkConf sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[2]");

        // Creating the SparkSession object
        SparkSession spark = SparkSession.builder().config(sparkConf).getOrCreate();
        logger.info("SparkSession created successfully");

        HiveWarehouseSession hive = HiveWarehouseSession.session(spark).build();
        logger.info("HiveWarehouseSession created successfully");

        String database_name = "hwc_db";
        String table_name = "employee";
        String database_table_name = database_name +"."+table_name;

        // Create a Database
        hive.createDatabase(database_name, true);

        // Display all databases
        hive.showDatabases().show(false);

        // Use database
        hive.setDatabase(database_name);

        // Display all tables
        hive.showTables().show(false);

        // Drop a table
        hive.dropTable(database_table_name, true, true);

        // Create a Table
        hive.createTable(database_table_name).ifNotExists()
        .column("id", "bigint")           
        .column("name", "string")           
        .column("age", "smallint")           
        .column("salary", "float").create();

        // Creating a dataset
        Dataset<EmployeeBean> employeeDF = getEmployeeDS(spark);
        employeeDF.printSchema();
        employeeDF.show(false);
        
        // Save the data
        saveEmployeeData(employeeDF, database_table_name);
        
        // Select the data
        Dataset<Row> empDF = getEmployeeData(hive, database_table_name);
        empDF.printSchema();
        empDF.show(false);

        logger.info("<Spark Hwc Integration> successfully finished");

        // Close the SparkSession
        spark.close();
        logger.info("SparkSession closed successfully");
    }

    public static Dataset<EmployeeBean> getEmployeeDS(SparkSession spark) {
        List<EmployeeBean> employeeData = new ArrayList<>();
        employeeData.add(new EmployeeBean(1l, "Ranga Reddy", 32, 80000.5f));
        employeeData.add(new EmployeeBean(2l, "Nishanth Reddy", 3, 180000.5f));
        employeeData.add(new EmployeeBean(3l, "Raja Sekhar Reddy", 59, 280000.5f));
        employeeData.add(new EmployeeBean(4l, "Manoj Reddy", 15, 8000.5f));
        employeeData.add(new EmployeeBean(5l, "Vasundra Reddy", 55, 580000.5f));

        return spark.createDataset(employeeData, Encoders.bean(EmployeeBean.class));
    }

    public static void saveEmployeeData(Dataset<EmployeeBean> employeeDF, String tableName) {
        employeeDF.write().format("com.hortonworks.spark.sql.hive.llap.HiveWarehouseConnector").
                mode("append").option("table", tableName).save();
    }
    
    public static Dataset<Row> getEmployeeData(HiveWarehouseSession hive, String tableName) {
        return hive.executeQuery("SELECT * FROM "+tableName);
    }
}