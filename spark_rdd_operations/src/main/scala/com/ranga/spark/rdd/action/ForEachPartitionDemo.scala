package com.ranga.spark.rdd.action

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object ForEachPartitionDemo extends App {

    def appName = this.getClass.getSimpleName

    val context = SparkContextUtil.getLocalSparkContext(appName)

    var numbers = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    var numberRDD = context.parallelize(numbers, 4)

    var forEachFn = (iter: Iterator[Int]) => {
        var builder = new StringBuilder
        while(iter.hasNext) {
            builder.append(iter.next() +" ")
        }
        println(builder.toString())
    }
    numberRDD.foreachPartition(forEachFn)
    context.stop()
}
