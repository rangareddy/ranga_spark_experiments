package com.ranga.spark.rdd.transformation

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object ZipWithIndexDemo extends App {

    def appName = this.getClass.getSimpleName

    val context = SparkContextUtil.getLocalSparkContext(appName)
    val numbers = List( 2, 3, 4, 5, 1, 6, 9)

    val numberRDD = context.parallelize(numbers)

    val zippedWithIndexRDD = numberRDD.zipWithIndex();
    val zippedWithIndexResult = zippedWithIndexRDD.collect().toList;
    println(s"zippedWithIndexResult ${zippedWithIndexResult}")

    context.stop()
}
