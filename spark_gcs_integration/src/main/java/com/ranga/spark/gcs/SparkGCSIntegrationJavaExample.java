package com.ranga.spark.gcs;

import org.apache.hadoop.conf.Configuration;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.*;

import java.util.ArrayList;
import java.util.List;

public class SparkGCSIntegrationJavaExample {
    public static void main(String[] args) {

        if (args.length < 5) {
            System.err.println("Usage   : SparkGCSIntegrationExample <PROJECT_ID> <BUCKET_NAME> <PRIVATE_KEY> <PRIVATE_KEY_ID> <CLIENT_EMAIL>");
            System.out.println("Example : SparkGCSIntegrationExample ranga-gcp-spark-project ranga-spark-gcp-bkt ranga_private_key ranga_private_key_id rangareddy@project.iam.gserviceaccount.com");
            System.exit(0);
        }

        String projectId = args[0];
        String bucketName = args[1];
        String privateKey = args[2];
        String privateKeyId = args[3];
        String clientEmail = args[4];

        // Creating the SparkConf object
        SparkConf sparkConf = new SparkConf().setAppName("Spark GCS Integration Java Example")
                .setIfMissing("spark.master", "local");

        // Creating the SparkSession object
        SparkSession spark = SparkSession.builder().config(sparkConf).getOrCreate();

        Configuration conf = spark.sparkContext().hadoopConfiguration();
        conf.set("fs.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem");
        conf.set("fs.AbstractFileSystem.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFS");
        conf.set("fs.gs.auth.service.account.enable", "true");
        conf.set("fs.gs.project.id", projectId);
        conf.set("fs.gs.auth.service.account.private.key", privateKey);
        conf.set("fs.gs.auth.service.account.private.key.id", privateKeyId);
        conf.set("fs.gs.auth.service.account.email", clientEmail);

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
        String gcs_dest_path = "gs://" + bucketName + "/employees";

        // Write the data as Orc
        String employeeOrcPath = gcs_dest_path + "/employee_orc";
        employeeDF.write().mode("overwrite").format("orc").save(employeeOrcPath);

        // Read the employee orc data
        Dataset<Row> employeeOrcData = spark.read().format("orc").load(employeeOrcPath);
        employeeOrcData.printSchema();
        employeeOrcData.show();

        // Write the data as Parquet
        String employeeParquetPath = gcs_dest_path + "/employee_parquet";
        employeeOrcData.write().mode("overwrite").format("parquet").save(employeeParquetPath);

        // Close the SparkSession
        spark.close();
        System.out.println("SparkSession stopped");
    }
}