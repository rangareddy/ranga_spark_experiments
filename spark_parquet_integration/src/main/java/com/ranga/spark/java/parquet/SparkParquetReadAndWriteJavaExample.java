package com.ranga.spark.java.parquet;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SQLContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* Ranga Reddy created on 13/07/20 */

class SparkParquetReadAndWriteJavaExample implements Serializable {

    public static void main(String[] args) {
        String appName = SparkParquetReadAndWriteJavaExample.class.getSimpleName().replace("$", "");
        SparkConf conf = new SparkConf().setIfMissing("spark.master", "local[3]").setAppName(appName);
        SparkContext sc = new SparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);
        writeParquet(sqlContext);
        readParquet(sqlContext);
    }

    public static void writeParquet(SQLContext sqlContext) {
        // Getting the DataFrame
        Dataset df = getDataFrame(sqlContext);

        // Write file to parquet
        df.write().mode("overwrite").parquet("Employees.parquet");
    }

    public static void readParquet(SQLContext sqlContext) {
        // Read parquet file
        Dataset dataFrame = sqlContext.read().parquet("Employees.parquet");
        display(dataFrame);
    }

    public static Dataset getDataFrame(SQLContext sqlContext) {
        List<Employee> employeeList = getEmployeeData();
        Dataset employeeDF = sqlContext.createDataFrame(employeeList, Employee.class);
        display(employeeDF);
        return employeeDF;
    }

    public static List<Employee> getEmployeeData() {
        List<Employee> employeeList = new ArrayList<>();

        employeeList.add(new Employee(1, "Ranga", 10000.00f, 1));
        employeeList.add(new Employee(2, "Vinod", 1000.00f, 1));
        employeeList.add(new Employee(3, "Nishanth", 500000.00f, 2));
        employeeList.add(new Employee(4, "Manoj", 25000.03f, 1));
        employeeList.add(new Employee(5, "Yashu", 1600.343f, 1));
        employeeList.add(new Employee(6, "Raja", 50000.2423f, 2));

        return employeeList;
    }

    public static void display(Dataset dataFrame) {
        // printSchema
        dataFrame.printSchema();
        // show contents
        dataFrame.show(5, false);
    }
}


