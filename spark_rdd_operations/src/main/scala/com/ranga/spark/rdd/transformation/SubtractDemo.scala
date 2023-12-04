package com.ranga.spark.rdd.transformation

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object SubtractDemo extends App {

    def appName = this.getClass.getSimpleName

    val context = SparkContextUtil.getLocalSparkContext(appName)

    val names1 = Seq("ranga", "ranga", "raja", "nishanth", "vinod")
    val names2 = Seq("nishanth", "vinod", "ranga", "nishanth", "vinod")

    val names1RDD = context.parallelize(names1)
    val names2RDD = context.parallelize(names2)

    val namesSubtractRDD = names1RDD.subtract(names2RDD)

    val subtractList = namesSubtractRDD.collect().toList
    println("subtractList "+subtractList)

    context.stop()
}
