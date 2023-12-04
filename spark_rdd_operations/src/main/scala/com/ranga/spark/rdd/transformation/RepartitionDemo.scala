package com.ranga.spark.rdd.transformation

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object RepartitionDemo extends App {

    def appName = this.getClass.getSimpleName

    val context = SparkContextUtil.getLocalSparkContext(appName)

    var numbers = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
    var numberRDD = context.parallelize(numbers)
    println(s"Default Partitions ${numberRDD.getNumPartitions}")

    val repartitionRDD = numberRDD.repartition(2)
    println(s"Repartition Partitions ${repartitionRDD.getNumPartitions}")

    context.stop()

}
