package com.ranga.spark.avro

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col

/* rangareddy.avula created on 11/06/20 */

object SparkAvroReadExample {
    def main(args: Array[String]): Unit = {

        val conf = new SparkConf().setIfMissing("spark.master", "local[*]").setAppName("Spark Avro Read Examples")
        val spark = SparkSession.builder().config(conf).getOrCreate();

        val employeeDF = spark.read.format("avro").load("src/main/resources/employees.avro");
        val filteredDF = employeeDF.filter(col("age") > 20)
        employeeDF.printSchema();
        employeeDF.foreach(employee => {println(employee);});

        spark.close();
    }
}
