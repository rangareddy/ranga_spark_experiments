package com.ranga.spark.rdd.action

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object TakeDemo extends App {

    def appName = this.getClass.getSimpleName

    val context = SparkContextUtil.getLocalSparkContext(appName)
    var numbers = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    var numberRDD = context.parallelize(numbers)

    var take5Result = numberRDD.take(5)
    println(s"take(5) values are ${take5Result.toList}")

    var take20Result = numberRDD.take(20)
    println(s"take(20) values are ${take20Result.toList}")

    context.stop()
}
