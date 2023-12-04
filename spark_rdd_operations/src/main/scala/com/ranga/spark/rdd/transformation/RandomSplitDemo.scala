package com.ranga.spark.rdd.transformation

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object RandomSplitDemo extends App {

    def appName = this.getClass.getSimpleName

    val context = SparkContextUtil.getLocalSparkContext(appName)

    var numbers = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    var numberRDD = context.parallelize(numbers)

    // def randomSplit(weights: Array[Double], seed: Long = Utils.random.nextLong): Array[RDD[T]]
    val randomSplitRDD = numberRDD.randomSplit(Array(0.7, 0.3))
    val firstSetRDD = randomSplitRDD(0)
    val secondSetRDD = randomSplitRDD(1)

    println(s"firstSet ${firstSetRDD.collect().toList}")
    println(s"secondSet ${secondSetRDD.collect().toList}")

    context.stop()

}
