package com.ranga.spark.avro

import com.ranga.spark.avro.bean.Employee
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession;

object SparkAvroWriteExample {
    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setIfMissing("spark.master", "local[*]").setAppName("Spark Avro Write Example")
        val spark = SparkSession.builder().config(conf).getOrCreate();

        val employeeList = List(Employee(1, "Ranga", 30, 10000.2f),
            Employee(2, "Vinod", 25, 1000.67f),
            Employee(3, "Nishanth", 2, 500000.76f),
            Employee(4, "Manoj", 14, 25000.45f),
            Employee(5, "Yashu", 10, 1600.98f),
            Employee(6, "Raja", 50, 50000.63f)
        )

        val employeeDF = spark.createDataFrame(employeeList);

        employeeDF.coalesce(1).write.format("avro").mode("overwrite").save("employees");

        spark.close();
    }
}