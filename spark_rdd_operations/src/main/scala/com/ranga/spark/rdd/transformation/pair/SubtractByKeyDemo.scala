package com.ranga.spark.rdd.transformation.pair

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

object SubtractByKeyDemo extends App {

    def appName = this.getClass.getSimpleName

    val context = SparkContextUtil.getLocalSparkContext(appName)

    val names1 = Seq("ranga", "ranga", "raja", "nishanth", "vinod")
    val names2 = Seq("nishanth", "vinod", "ranga", "nishanth", "vinod")

    val names1RDD = context.parallelize(names1)
    val names2RDD = context.parallelize(names2)

    var mappingFc = (str:String) => (str, str.length)

    val namesMapped1RDD = names1RDD.map(mappingFc)
    val namesMapped2RDD = names2RDD.map(mappingFc)

    val subtractRDD = namesMapped1RDD.subtractByKey(namesMapped2RDD)

    val subtractList = subtractRDD.collect().toList
    println("Subtracted Results "+subtractList)
    
    context.stop()
}
