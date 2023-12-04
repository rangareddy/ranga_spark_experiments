package com.ranga.spark.rdd.action

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object CountByValueDemo extends App {

    def appName = this.getClass.getSimpleName

    val context = SparkContextUtil.getLocalSparkContext(appName)
    var numbers = Array(1, 2, 3, 2, 8,7, 4, 5, 6, 7, 8, 9, 10)

    var numberRDD = context.parallelize(numbers)
    val countByValueResult = numberRDD.countByValue().toList

    println(s"countByValueResult $countByValueResult")
    context.stop()
}
