package com.ranga.spark.rdd.transformation.pair

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object SampleByKeyDemo extends App {

    def appName = this.getClass.getSimpleName

    val context = SparkContextUtil.getLocalSparkContext(appName)

    // Note: keys must exist otherwise we will get java.util.NoSuchElementException: key not found: 2
    val students = Seq((1, "ranga"), (2, "yasu"), (3, "raja"), (4, "nishanth"))
    val studentMarks = Seq((1, 40.0d), (3, 70.0d), (4, 50.0d), (5, 60.0d), (2, 90.0)).toMap

    val studentsRDD = context.parallelize(students)
    val sampleByKeyRDD = studentsRDD.sampleByKey(false, studentMarks, 3)

    val sampleByKeyList = sampleByKeyRDD.collect().toList
    println("sampleByKeyList "+sampleByKeyList)
    
    context.stop()
}
