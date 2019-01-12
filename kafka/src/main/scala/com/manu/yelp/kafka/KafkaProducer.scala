package com.manu.yelp.kafka

import java.util.{HashMap, Properties}

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.sql.DataFrame
import scala.io.Source

object KafkaProducer {

  val producer = new KafkaProducer[String,String](getKafkaConfig)

  def main(args: Array[String]): Unit = {

    if(args.length < 4) {
      System.err.println("Usage: KafkaProducer <BrokerList> <topic> <messgaePerSec> <wordsPermessage>")
      System.exit(1)
    }

    ConfigProvider.setConfig("/kafkaConfig.properties")

//Reading json files from Data folder

    val businessDF = Source.fromFile("")
    val userDF = DataLoader.getData("Data/json/yelp_academic_dataset_user.json")

    while(true){
      try{

        for(line <- Source.fromFile("path").getLines()){
          produce("topicName",line)
        }
        Thread.sleep(1000)
      } catch {
        case e:Exception => e.printStackTrace()
      } finally {
        producer.close()
      }

    }
  }

  def produce(topic:String,data:String)={

    producer.send(new ProducerRecord[String,String](topic,data))
  }
}
