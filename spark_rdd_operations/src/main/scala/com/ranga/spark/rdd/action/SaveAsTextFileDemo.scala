package com.ranga.spark.rdd.action

import com.ranga.spark.rdd.util.SparkContextUtil
import org.apache.hadoop.io.compress.GzipCodec
import org.apache.spark.rdd.RDD

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object SaveAsTextFileDemo extends App {

    def appName = this.getClass.getSimpleName
    val context = SparkContextUtil.getLocalSparkContext(appName)

    var numbers = Array(1, 6,  3, 5, 7,  10, 4, 9, 11, 2, 12, 13, 14, 8, 15)
    var numberRDD = context.parallelize(numbers)
    val resultRDD = numberRDD.coalesce(1)

    // saving file without compression
    resultRDD.saveAsTextFile("text_file_data")

    // saving file with compression
    resultRDD.saveAsTextFile("text_file_data_gzip", classOf[GzipCodec])

    // saving file to hdfs
   // resultRDD.saveAsTextFile("hdfs://localhost:8020/user/rangareddy/test", classOf[GzipCodec])

    context.stop()

}
