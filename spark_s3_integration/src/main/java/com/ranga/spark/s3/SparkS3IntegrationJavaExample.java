package com.ranga.spark.s3;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.*;

import java.util.ArrayList;
import java.util.List;

public class SparkS3IntegrationJavaExample {

    public static void main(String[] args) {

        if(args.length < 3) {
            System.err.println("Usage   : SparkS3IntegrationJavaExample <AWS_ACCESS_KEY_ID> <AWS_SECRET_ACCESS_KEY> <BUCKET_NAME>");
            System.out.println("Example : SparkS3IntegrationJavaExample ranga_aws_access_key ranga_aws_secret_access_key ranga-spark-s3-bkt");
            System.exit(0);
        }

        String awsAccessKey = args[0];
        String awsSecretKey = args[1];
        String bucketName = args[2];

        // Creating the SparkConf object
        SparkConf sparkConf = new SparkConf().setAppName("Spark S3 Integration Java Example")
                .set("spark.hadoop.fs.s3a.access.key", awsAccessKey)
                .set("spark.hadoop.fs.s3a.secret.key", awsSecretKey)
                .set("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
                .set("spark.hadoop.mapreduce.fileoutputcommitter.algorithm.version", "2")
                .set("spark.speculation", "false")
                .set("spark.hadoop.mapreduce.fileoutputcommitter.cleanup-failures.ignored", "true")
                .set("fs.s3a.experimental.input.fadvise", "random")
                .setIfMissing("spark.master", "local");

        // Creating the SparkSession object
        SparkSession spark = SparkSession.builder().config(sparkConf).getOrCreate();
        System.out.println("SparkSession Created successfully");

        List<Employee> employeeData = new ArrayList<>();
        employeeData.add(new Employee(1, "Ranga", 32, 245000.30));
        employeeData.add(new Employee(2, "Nishanth", 2, 345000.10));
        employeeData.add(new Employee(3, "Raja", 32, 245000.86));
        employeeData.add(new Employee(4, "Mani", 14, 45000.00));

        Dataset<Row> employeeDF = spark.createDataFrame(employeeData, Employee.class);
        employeeDF.printSchema();
        employeeDF.show();

        // Define the s3 destination path
        String s3_dest_path = "s3a://"+bucketName+"/employees";

        // Write the data as Orc
        String employeeOrcPath = s3_dest_path + "/employee_orc";
        employeeDF.write().mode("overwrite").format("orc").save(employeeOrcPath);

        // Read the employee orc data
        Dataset<Row> employeeOrcData = spark.read().format("orc").load(employeeOrcPath);
        employeeOrcData.printSchema();
        employeeOrcData.show();

        // Write the data as Parquet
        String employeeParquetPath = s3_dest_path + "/employee_parquet";
        employeeOrcData.write().mode("overwrite").format("parquet").save(employeeParquetPath);

        // Close the SparkSession
        spark.close();
        System.out.println("SparkSession stopped");
    }
}