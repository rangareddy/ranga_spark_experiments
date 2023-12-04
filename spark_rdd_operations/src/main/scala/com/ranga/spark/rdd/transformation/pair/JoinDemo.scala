package com.ranga.spark.rdd.transformation.pair

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object JoinDemo extends App {

    def appName = this.getClass.getSimpleName

    val context = SparkContextUtil.getLocalSparkContext(appName)
    val studentMarks1 = Seq(("ranga", 55), ("ranga", 56), ("raja", 57), ("nishanth", 58), ("nishanth", 59), ("vinod", 54))
    val studentMarks2 = Seq(("ranga", 80), ("raja", 64), ("nishanth", 84), ("vinod", 52), ("vinod", 77), ("Yasu", 67))
    val studentMarks1RDD = context.parallelize(studentMarks1)
    val studentMarks2RDD = context.parallelize(studentMarks2)

    val studentsMarksJoinedRDD = studentMarks1RDD.join(studentMarks2RDD)
    studentsMarksJoinedRDD.collect().foreach(res => println(res))

    val studentsMarksLeftJoinedRDD = studentMarks1RDD.leftOuterJoin(studentMarks2RDD)
    studentsMarksLeftJoinedRDD.collect().foreach(res => println(res))

    val studentsMarksRightJoinedRDD = studentMarks1RDD.rightOuterJoin(studentMarks2RDD)
    studentsMarksRightJoinedRDD.collect().foreach(res => println(res))

    context.stop()
}
