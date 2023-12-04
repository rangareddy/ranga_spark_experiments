package com.ranga.spark.rdd.transformation

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object ZipWithUniqueIdDemo extends App {

    def appName = this.getClass.getSimpleName

    val context = SparkContextUtil.getLocalSparkContext(appName)
    val numbers = List( 2, 3, 4, 5, 1, 6, 9)

    val numberRDD = context.parallelize(numbers)

    val zipWithUniqueIdResult = numberRDD.zipWithUniqueId().collect().toList;
    println(s"zipWithUniqueIdResult ${zipWithUniqueIdResult}")

    context.stop()
}
