package com.ranga.spark.rdd.transformation

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object PipeDemo extends App {

    def appName = this.getClass.getSimpleName

    val context = SparkContextUtil.getLocalSparkContext(appName)

    var numbers = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    var numberRDD = context.parallelize(numbers)

    val pipeScript = "./src/main/resources/pipe_script.sh"
    val filteredRDD = numberRDD.pipe(pipeScript)

    // Collect the filtered data
    val filteredData = filteredRDD.collect()

    // Print the filtered data
    filteredData.foreach(println)

    context.stop()

}
