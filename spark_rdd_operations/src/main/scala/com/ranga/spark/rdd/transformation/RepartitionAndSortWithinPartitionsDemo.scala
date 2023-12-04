package com.ranga.spark.rdd.transformation

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object RepartitionAndSortWithinPartitionsDemo extends App {

    def appName = this.getClass.getSimpleName

    val context = SparkContextUtil.getLocalSparkContext(appName)

    val elementsRDD = context.parallelize(List( (2, "computer"), (4,"cat"), (3, "mouse"), (4, "tv"), (8, "screen"),  (7, "cup"), (6, "book"),(1, "heater"), (8, "headphone")), 3)
    val rangePartitioner = new org.apache.spark.RangePartitioner(4, elementsRDD)

    // Range partition without sorting
    {
        val partitionedRDD = elementsRDD.partitionBy(rangePartitioner)
        val partitionResult = partitionedRDD.mapPartitionsWithIndex(myfunc).collect.toList
        println(s"Result $partitionResult")
    }

    // Range partition with sorting
    {
        val partitionedRDD = elementsRDD.repartitionAndSortWithinPartitions(rangePartitioner)
        val partitionResult = partitionedRDD.mapPartitionsWithIndex(myfunc).collect.toList
        println(s"Result $partitionResult")
    }

    def myfunc(index: Int, iter: Iterator[(Int, String)]) : Iterator[String] = {
        iter.map(x => "[partID:" +  index + ", val: " + x + "]")
    }

    context.stop()

}
