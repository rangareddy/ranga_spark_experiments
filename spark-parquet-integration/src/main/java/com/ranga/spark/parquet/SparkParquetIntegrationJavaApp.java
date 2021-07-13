package com.ranga.spark.parquet;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.*;
import org.apache.log4j.Logger;
import java.io.Serializable;
import java.util.*;

/**
 * @author Ranga Reddy
 * Version: 1.0
 * Created : 07/13/2021
 */

public class SparkParquetIntegrationJavaApp implements Serializable {

    private static final Logger logger = Logger.getLogger(SparkParquetIntegrationJavaApp.class.getName());

    public static void main(String[] args) {


        String appName = "Spark Parquet Integration";
        
        // Creating the SparkConf object
        SparkConf sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[2]");

        // Creating the SparkSession object
        SparkSession spark = SparkSession.builder().config(sparkConf).getOrCreate();
        logger.info("SparkSession created successfully");

        // Creating a dataset
        Dataset<EmployeeBean> employeeDF = getEmployeeDS(spark);
        employeeDF.printSchema();
        employeeDF.show(false);

        // parquet
        String parquetFilePath = "/tmp/parquet_data";
        saveData(employeeDF, "parquet", parquetFilePath);

        Dataset<Row> parquetEmployeeDF = loadData(spark, "parquet", parquetFilePath);
        display(parquetEmployeeDF);


        logger.info("<Spark Parquet Integration> successfully finished");

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

    public static void display(Dataset<Row> dataset) {
        dataset.printSchema();
        dataset.show(false);
    }

    public static Dataset<Row> loadData(SparkSession spark, String format, String path) {
        Dataset<Row> employeeDF = spark.read().format(format).load(path);
        return employeeDF;
    }

    public static void saveData(Dataset<EmployeeBean> employeeDF, String format, String path) {
        employeeDF.coalesce(1).write().format(format).mode("overwrite").save(path);
    }
}