package com.ranga.spark.rdd.transformation

import com.ranga.spark.rdd.util.SparkContextUtil

/**
 * User: Ranga Reddy
 * Date: 04/12/23
 * Time: 11:35 am
 */

/*
    sample() - Randomly selects a fraction of the items of a RDD and returns them in a new RDD.

    def sample(withReplacement: Boolean, fraction: Double, seed: Long = Utils.random.nextLong): RDD[T]

    withReplacement - can elements be sampled multiple times (replaced when sampled out)
    fraction - expected size of the sample as a fraction of this RDD's size without replacement: probability that each element is chosen; fraction must be [0, 1] with replacement: expected number of times each element is chosen; fraction must be greater than or equal to 0
    seed - seed for the random number generator
*/
object SampleDemo extends App {

    def appName = this.getClass.getSimpleName
    val context = SparkContextUtil.getLocalSparkContext(appName)

    var numbers = Array(1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 14, 15)
    var numberRDD = context.parallelize(numbers)

    val sampleRDDWithReplacement = numberRDD.sample(true, 0.5, 0)
    val sampleList1 = sampleRDDWithReplacement.collect().toList

    println("SampleRDDWithReplacement " + sampleList1)

    val sampleRDDWithoutReplacement = numberRDD.sample(false, 0.5, 10)
    val sampleList2 = sampleRDDWithoutReplacement.collect().toList

    println("SampleRDDWithoutReplacement " + sampleList2)

    context.stop()

}
