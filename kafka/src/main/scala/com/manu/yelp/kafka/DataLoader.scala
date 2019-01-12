package com.manu.yelp.kafka

import com.manu.yelp.utils
import org.apache.spark.sql.DataFrame

object DataLoader extends SparkConfig{

  def getData(path: String): DataFrame = {

    val df = spark.read.json(path)
    df
  }
}
