package com.ranga.spark.rdd.transformation

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object PersistAndUnpersistDemo extends App {

    def appName = this.getClass.getSimpleName

    val context = SparkContextUtil.getLocalSparkContext(appName)
    val numbers = List( 2, 3, 4, 5, 1, 6, 9)

    val numberRDD = context.parallelize(numbers)
    // persisting rdd
    val persistedRDD = numberRDD.persist()

    val take10 = persistedRDD.take(10).toList
    val take5 = persistedRDD.take(5).toList
    val filterNum = persistedRDD.filter( num => num % 3 == 0).collect().toList

    Thread.sleep(10000)

    // unpersisting rdd
    persistedRDD.unpersist()

    Thread.sleep(5000)

    context.stop()
}
