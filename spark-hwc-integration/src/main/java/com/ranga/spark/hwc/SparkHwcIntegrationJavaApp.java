package com.ranga.spark.hwc;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;

public class SparkHwcIntegrationJavaApp implements Serializable {
    public static void main(String[] args) {

        String appName = "SparkHwcIntegrationApp Example";

        // Creating the SparkConf object
        SparkConf sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[*]");

        // Creating the SparkSession object
        SparkSession spark = SparkSession.builder().config(sparkConf).getOrCreate();
        System.out.println("SparkSession Created successfully");

        // Creating a dataset
        Dataset<Long> dataset = spark.range(1, 1000);
        dataset.printSchema();
        dataset.show();

        // Close the SparkSession
        spark.close();
        System.out.println("SparkSession closed successfully");
    }
}