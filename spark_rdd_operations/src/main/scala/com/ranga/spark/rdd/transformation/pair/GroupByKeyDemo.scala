package com.ranga.spark.rdd.transformation.pair

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object GroupByKeyDemo extends App {

    def appName = this.getClass.getSimpleName

    val context = SparkContextUtil.getLocalSparkContext(appName)
    val studentMarks = Seq(("ranga", 55), ("ranga", 56), ("raja", 57), ("nishanth", 58), ("nishanth", 59), ("vinod", 54), ("ranga", 80), ("nishanth", 84), ("vinod", 52))
    val studentMarksRDD = context.parallelize(studentMarks)

    val groupByKeyList = studentMarksRDD.groupByKey().collect().toList
    println("groupByKeyList  "+groupByKeyList)
    context.stop()
}
