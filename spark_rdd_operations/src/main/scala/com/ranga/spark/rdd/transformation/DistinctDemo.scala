package com.ranga.spark.rdd.transformation

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object DistinctDemo extends App {

    def appName = this.getClass.getSimpleName
    val context = SparkContextUtil.getLocalSparkContext(appName)

    val numbers = List(1, 2, 3, 4, 5, 3, 4, 6, 8, 2)
    val numberRDD = context.parallelize(numbers)

    val distinctNumbers = numberRDD.distinct().collect().toList

    println(s"Distinct Numbers ${distinctNumbers}")
    println(s"Original List size ${numbers.size}, Distinct List size ${distinctNumbers.size}")

    context.stop()
}
