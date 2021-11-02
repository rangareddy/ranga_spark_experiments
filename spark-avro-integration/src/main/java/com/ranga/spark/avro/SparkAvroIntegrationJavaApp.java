package com.ranga.spark.avro;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.*;
import org.apache.log4j.Logger;
import java.io.Serializable;
import java.util.*;

/**
 * @author Ranga Reddy
 * Version: 1.0
 * Created : 10/18/2021
 */

public class SparkAvroIntegrationJavaApp implements Serializable {

    private static final Logger logger = Logger.getLogger(SparkAvroIntegrationJavaApp.class.getName());

    public static void main(String[] args) {

        String appName = "Spark Avro Integration";
        
        // Creating the SparkConf object
        SparkConf sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[2]");

        // Creating the SparkSession object
        SparkSession spark = SparkSession.builder().config(sparkConf).getOrCreate();
        logger.info("SparkSession created successfully");

        String avroFilePath = "/tmp/avro_data";

        // Get the Employee Dataset
        Dataset<EmployeeBean> employeeDF = getEmployeeDS(spark);
        display(employeeDF);

        // write avro data
        employeeDF.coalesce(1).write().format("avro").mode("overwrite").save(avroFilePath);

        // read avro data
        Dataset<Row> avroEmployeeDF = spark.read().format("avro").load(avroFilePath);
        display(avroEmployeeDF);

        logger.info("<Spark Avro Integration> successfully finished");

        // Close the SparkSession
        spark.close();
        logger.info("SparkSession closed successfully");
    }

    // Get the Employee Dataset
    public static Dataset<EmployeeBean> getEmployeeDS(SparkSession spark) {
        List<EmployeeBean> employeeData = new ArrayList<>();
        employeeData.add(new EmployeeBean(1l, "Ranga Reddy", 32, 80000.5f));
        employeeData.add(new EmployeeBean(2l, "Nishanth Reddy", 3, 180000.5f));
        employeeData.add(new EmployeeBean(3l, "Raja Sekhar Reddy", 59, 280000.5f));
        employeeData.add(new EmployeeBean(4l, "Manoj Reddy", 15, 8000.5f));
        employeeData.add(new EmployeeBean(5l, "Vasundra Reddy", 55, 580000.5f));
        return spark.createDataset(employeeData, Encoders.bean(EmployeeBean.class));
    }

    // Display the Dataset
    public static void display(Dataset<Row> dataset) {
        dataset.printSchema();
        dataset.show(false);
    }

}