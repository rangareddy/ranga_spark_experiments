package com.ranga.spark.hbase

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql._
import org.apache.spark.sql.execution.datasources.hbase._
import org.apache.spark.SparkConf

import scala.collection.mutable

object SparkHBaseIntegrationApp extends App {

    def appName = this.getClass.getSimpleName

    val sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[4]")
    
    // Creating the SparkSession
    val spark = SparkSession.builder.config(sparkConf).getOrCreate()

    // define schema
    def catalog =
        s"""{
        	"table":{"namespace":"default", "name":"employee"},
         	"rowkey":"key",
        	"columns":{
	        	"key":{"cf":"rowkey", "col":"key", "type":"string"},
	         	"name":{"cf":"per", "col":"name", "type":"string"},
	         	"age":{"cf":"per", "col":"age", "type":"string"},
	         	"designation":{"cf":"prof", "col":"designation", "type":"string"},
	         	"salary":{"cf":"prof", "col":"salary", "type":"string"}
         	}
        }""".stripMargin

    // Read HBase data
    val employeeDF = spark.read.options(Map(HBaseTableCatalog.tableCatalog -> catalog))
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .load()

    employeeDF.printSchema()
    employeeDF.show(truncate=false)
    println(s"Total no of Employees : ${employeeDF.count()}")
    
    // Closing the sparksession
    spark.close()
}
