package com.manu.yelp.utils

import org.apache.spark.sql.SparkSession

trait SparkConfig {

  val spark: SparkSession = SparkSession.builder()
    .master("local[4]")
    .config("spark.shuffle.spill","false")
    .config("spark.rdd.compress", "true")
    .config("spark.storage.memoryFraction", "1")
    .config("spark.shuffle.consolidateFiles", "true")
    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .config("spark.sql.crossJoin.enabled", "true")
    .enableHiveSupport().getOrCreate()

}
