package com.ranga.spark.rdd.transformation

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object FlatMapDemo extends App {

    def appName = this.getClass.getSimpleName
    val context = SparkContextUtil.getLocalSparkContext(appName)

    var numbers = Array(Array(1, 2, 3, 4), Array(5, 6, 7, 8, 10), Array(11, 12, 13, 14, 15))
    var numberRDD = context.parallelize(numbers)

    val flatMapResult = numberRDD.flatMap(num => num).collect().toList

    println(s"flatMapResult $flatMapResult")
    context.stop()

}
