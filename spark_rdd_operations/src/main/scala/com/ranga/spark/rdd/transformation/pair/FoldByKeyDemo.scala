package com.ranga.spark.rdd.transformation.pair

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object FoldByKeyDemo extends App {

    def appName = this.getClass.getSimpleName

    val context = SparkContextUtil.getLocalSparkContext(appName)

    var names = Array("ranga", "yaswanth", "raja", "nishanth", "reddy", "manoj");
    var namesRDD = context.parallelize(names)
    var namesPairRDD = namesRDD.map(name => (name.length, name))
    val foldByKeyResult = namesPairRDD.foldByKey("")(_+_).collect().toList
    println(s"foldByKeyResult ${foldByKeyResult}")

    context.stop()

}
