package com.cloudera.spark.hwc;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.*;
import com.hortonworks.hwc.HiveWarehouseSession;

import java.util.ArrayList;
import java.util.List;

public class SparkHWCJavaExample {

    private final static String DATABASE_NAME = "hwc_db";
    private final static String TABLE_NAME = "employee";
    private final static String DATABASE_TABLE_NAME = DATABASE_NAME +"."+TABLE_NAME;

    public static void main(String[] args) {

        SparkConf sparkConf = new SparkConf().
                setSparkHome("Spark CDP HWC Example")
                .setIfMissing("spark.master", "local");

        SparkSession spark = SparkSession.builder().config(sparkConf)
                .enableHiveSupport()
                .getOrCreate();

        HiveWarehouseSession hive = HiveWarehouseSession.session(spark).build();

        // Create a Database
        hive.createDatabase(DATABASE_NAME, true);

        // Display all databases
        hive.showDatabases().show(false);

        // Use database
        hive.setDatabase(DATABASE_NAME);

        // Display all tables
        hive.showTables().show(false);

        // Drop a table
        hive.dropTable(DATABASE_TABLE_NAME, true, true);

        // Create a Table
        hive.createTable("employee").ifNotExists().column("id", "bigint").column("name", "string").
                column("age", "smallint").column("salary", "double").create();

        // Insert the data
        Encoder<Employee> encoder = Encoders.bean(Employee.class);
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee(1, "Ranga", 32, 245000.30));
        employeeList.add(new Employee(2, "Nishanth", 2, 345000.10));
        employeeList.add(new Employee(3, "Raja", 32, 245000.86));
        employeeList.add(new Employee(4, "Mani", 14, 45000));

        Dataset<Employee> employeeDF = spark.createDataset(employeeList, encoder);
        employeeDF.write().format("com.hortonworks.spark.sql.hive.llap.HiveWarehouseConnector").
                mode("append").option("table", "hwc_db.employee").save();

        // Select the data
        Dataset<Row> empDF = hive.executeQuery("SELECT * FROM "+DATABASE_TABLE_NAME);
        empDF.printSchema();
        empDF.show();

        // Close the SparkSession
        spark.close();
    }
}