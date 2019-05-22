package com.manu.yelp.sink

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql._
import org.apache.spark.sql.cassandra._

import com.datastax.spark.connector.cql.CassandraConnectorConf
import com.datastax.spark.connector.rdd.ReadConf
import com.datastax.spark.connector._


object CassandraSink {

  def main(args: Array[String]): Unit = {

    val host = "127.0.0.1:9042"
    val clusterName = "Test Cluster"
    val keySpace = "yelpdata"
    val tableName = "business"

    val spark = SparkSession.builder().appName("KafkaCassandraSink").master("local[*]").getOrCreate()

    spark.setCassandraConf(clusterName,CassandraConnectorConf.ConnectionHostParam.option(host))

    spark.readStream.format("rate").load()
      .writeStream
      .foreachBatch { (batchDF: DataFrame, batchId: Long) =>

        batchDF.write
          .cassandraFormat(tableName, keySpace)
          .option("cluster", clusterName)
          .mode("append")
          .save()
      }
      .outputMode("update")
      .start()
  }
}
