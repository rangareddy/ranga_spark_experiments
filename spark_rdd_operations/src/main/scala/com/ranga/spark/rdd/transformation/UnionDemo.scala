package com.ranga.spark.rdd.transformation

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object UnionDemo extends App {

    def appName = this.getClass.getSimpleName

    val context = SparkContextUtil.getLocalSparkContext(appName)

    val numbers1 = List(1, 2, 3, 4, 5)
    val numbers2 = List(2, 4, 5, 6, 9, 10)

    val number1RDD = context.parallelize(numbers1)
    val number2RDD = context.parallelize(numbers2)

    {
        val unionNumberRDD = number1RDD.union(number2RDD)
        val unionResult = unionNumberRDD.collect().toList
        println(s"unionResult  ${unionResult}")
    }

    {
        val unionNumberRDD = number1RDD ++ number2RDD
        val unionResult = unionNumberRDD.collect().toList
        println(s"unionResult ${unionResult}")
    }


    context.stop()
}
