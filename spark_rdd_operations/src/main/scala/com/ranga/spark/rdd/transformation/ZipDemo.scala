package com.ranga.spark.rdd.transformation

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object ZipDemo extends App {

    def appName = this.getClass.getSimpleName

    val context = SparkContextUtil.getLocalSparkContext(appName)

    // Note: Can only zip RDDs with same number of elements in each partition
    val numbers1 = List(1, 2, 3, 4, 5)
    val numbers2 = List(2, 4, 5, 6, 9)

    val number1RDD = context.parallelize(numbers1)
    val number2RDD = context.parallelize(numbers2)

    val zippedRDD = number1RDD.zip(number2RDD).collect().toList
    println(s"zippedRDD ${zippedRDD}")

    context.stop()
}
