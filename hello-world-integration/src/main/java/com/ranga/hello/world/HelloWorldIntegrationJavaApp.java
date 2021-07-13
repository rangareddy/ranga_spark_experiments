package com.ranga.hello.world;

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

public class HelloWorldIntegrationJavaApp implements Serializable {

    private static final Logger logger = Logger.getLogger(HelloWorldIntegrationJavaApp.class.getName());

    public static void main(String[] args) {


        String appName = "Hello World Integration";
        
        // Creating the SparkConf object
        SparkConf sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[2]");

        // Creating the SparkSession object
        SparkSession spark = SparkSession.builder().config(sparkConf).getOrCreate();
        logger.info("SparkSession created successfully");

        // Creating a dataset
        Dataset<EmployeeBean> employeeDF = getEmployeeDS(spark);
        employeeDF.printSchema();
        employeeDF.show(false);
        
        long employeeCount = getEmployeeCount(employeeDF);
        logger.info("Employees count "+employeeCount);

        logger.info("<Hello World Integration> successfully finished");

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
    
    public static Long getEmployeeCount(Dataset<EmployeeBean> employeeDataset) {
        return employeeDataset.count();
    }
}