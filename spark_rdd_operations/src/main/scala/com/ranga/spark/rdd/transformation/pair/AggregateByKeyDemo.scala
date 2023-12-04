package com.ranga.spark.rdd.transformation.pair

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

/**
 * Spark aggregateByKey function aggregates the values of each key, using given combine functions and a neutral “zero value”
 * The aggregateByKey function aggregates values for each key and and returns a different type of value for that key.
 *
 * The aggregateByKey function requires 3 parameters:
    a) An initial ‘zero’ value that will not effect the total values to be collected. For example if we were adding numbers the initial value would be 0. Or in the case of collecting unique elements per key, the initial value would be an empty set.
    b) A combining function accepting two parameters. The second parameter is merged into the first parameter. This function combines/merges values within a partition.
    c) A merging function function accepting two parameters. In this case the parameters are merged into one. This step merges values across partitions.
 */

object AggregateByKeyDemo extends App {

    def appName = this.getClass.getSimpleName

    val context = SparkContextUtil.getLocalSparkContext(appName)
    val studentMarks = Seq(("ranga", 55), ("ranga", 56), ("raja", 57), ("nishanth", 58), ("nishanth", 59), ("vinod", 54), ("ranga", 80), ("nishanth", 84), ("vinod", 52))
    val studentMarksRDD = context.parallelize(studentMarks, 4)

    val initialCount = 0;
    val sumOfMarks = (marks1: Int, marks2: Int) =>  { println("marks1: "+marks1 +", marks2: "+marks2); marks1 + marks2}
    val sumOfMarksPartition = (p1: Int, p2: Int) => { println("p1: "+p1 +", p2: "+p2); p1 + p2}

    val studentsMarksAggregateByKeyRDD = studentMarksRDD.aggregateByKey (initialCount)( sumOfMarks, sumOfMarksPartition)
    val aggregateByKeyList = studentsMarksAggregateByKeyRDD.sortByKey().collect().toList

    println(s"aggregateByKeyList $aggregateByKeyList")
    context.stop()
}
