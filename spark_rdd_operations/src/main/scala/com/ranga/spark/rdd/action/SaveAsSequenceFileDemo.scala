package com.ranga.spark.rdd.action

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object SaveAsSequenceFileDemo extends App {

    def appName = this.getClass.getSimpleName
    val context = SparkContextUtil.getLocalSparkContext(appName)

    val animalList = Array(("snake",1), ("cat",3), ("dog",4), ("frog",2), ("ant",5))
    val animalRDD = context.parallelize(animalList)
    animalRDD.coalesce(1).saveAsSequenceFile("sequence_file_data")

    context.stop()

}
