package com.manu.yelp.kafka

import org.apache.spark.sql.DataFrame

object DataLoader extends SparkConfig{

  def getData(path: String): DataFrame = {

    val df = spark.read.json(path)
    df
  }
}
