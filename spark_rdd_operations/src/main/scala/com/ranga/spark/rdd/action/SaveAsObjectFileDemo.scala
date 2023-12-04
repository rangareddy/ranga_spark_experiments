package com.ranga.spark.rdd.action

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object SaveAsObjectFileDemo extends App {

    def appName = this.getClass.getSimpleName
    val context = SparkContextUtil.getLocalSparkContext(appName)

    var numbers = Array(1, 6,  3, 5, 7,  10, 4, 9, 11, 2, 12, 13, 14, 8, 15)
    var numberRDD = context.parallelize(numbers)
    numberRDD.coalesce(1).saveAsObjectFile("object_file_data")

    context.stop()

}
