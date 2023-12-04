package com.ranga.spark.rdd.transformation.pair

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object CombineByKeyDemo extends App {

    def appName = this.getClass.getSimpleName

    val context = SparkContextUtil.getLocalSparkContext(appName)
    val studentMarks = Seq(("ranga", 55), ("ranga", 56), ("raja", 57), ("nishanth", 58), ("nishanth", 59), ("vinod", 54), ("ranga", 80), ("nishanth", 84), ("vinod", 52))
    val studentMarksRDD = context.parallelize(studentMarks)

    val combiner = (marks:Int) => {
        println(s"combiner -> ${marks}")
        (marks, 1)
    }

    val mergeValue = (acc: (Int, Int), v:Int) => {
        println(s"""Merge value : (${acc._1} + ${v}, ${acc._2} + 1)""")
        (acc._1 + v, acc._2 + 1)
    }

    val mergeCombiners = (acc1: (Int, Int), acc2: (Int, Int)) => {
       println(s"""Merge Combiner : (${acc1._1} + ${acc2._1}, ${acc1._2} + ${acc2._2})""")
       (acc1._1 + acc2._1, acc1._2 + acc2._2)
    }

    val combineByKeyResult = studentMarksRDD.combineByKey(combiner, mergeValue, mergeCombiners).collect().toList
    println(s"combineByKeyResult $combineByKeyResult")

    context.stop()
}
