package com.ranga.spark.rdd.util

import org.apache.spark.{SparkConf, SparkContext}

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object SparkContextUtil {

    def getSparkContext(appName:String, master:String) : SparkContext = {
        val conf:SparkConf = new SparkConf().setAppName(appName).setMaster(master)
        val sc:SparkContext = new SparkContext(conf)
        sc
    }

    def getLocalSparkContext(appName:String) : SparkContext = {
        getSparkContext(appName, "local[*]")
    }
}
