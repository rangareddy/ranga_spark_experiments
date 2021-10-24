package com.ranga.spark.hello.world

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import SparkHelloWorldIntegrationApp.{getEmployeeCount, getEmployeeDS}

/**
 * @author Ranga Reddy
 * Version: 1.0
 * Created : 10/18/2021
 */

class SparkHelloWorldIntegrationAppTest extends FunSuite with BeforeAndAfterAll {

    @transient var spark: SparkSession = _

    override def beforeAll(): Unit = {
        val appName = "Spark Hello World IntegrationTest"
        val sparkConf = new SparkConf().setAppName(appName).setIfMissing("spark.master", "local[2]")
        spark = SparkSession.builder().config(sparkConf).getOrCreate()
    }

    override def afterAll(): Unit = {
        if (spark != null) spark.stop()
    }

    test("Get Employee DS") {
        val employeeDS = getEmployeeDS(spark)
        val rangeCount = employeeDS.count()
        assert(rangeCount == 5, "Employee count should be 5")
    }

    test("Count Employee DS") {
        val rangeDS = getEmployeeDS(spark)
        val rangeCount = getEmployeeCount(rangeDS)
        assert(rangeCount == 5, "Employee count should be 1000")
    }
}