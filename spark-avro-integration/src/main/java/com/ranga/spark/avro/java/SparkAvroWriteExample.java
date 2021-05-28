package com.ranga.spark.avro.java;

import com.ranga.spark.avro.java.bean.Employee;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.ArrayList;
import java.util.List;

/* rangareddy.avula created on 11/06/20 */
public class SparkAvroWriteExample {

    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setIfMissing("spark.master", "local[*]").setAppName("Spark Avro Write Examples");
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee(1, "Ranga", 30, 10000.2f));
        employeeList.add(new Employee(2, "Vinod", 25, 1000.67f));
        employeeList.add(new Employee(3, "Nishanth", 2, 500000.76f));
        employeeList.add(new Employee(4, "Manoj", 14, 25000.45f));
        employeeList.add(new Employee(5, "Yashu",  10, 1600.98f));
        employeeList.add(new Employee(6, "Raja", 50, 50000.63f));

        Dataset<Row> employeeDF = spark.createDataFrame(employeeList, Employee.class);
        employeeDF.printSchema();
        employeeDF.coalesce(1).write().format("avro").mode("overwrite").save("employees");
        spark.close();
    }
}
