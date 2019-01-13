package com.manu.yelp.kafka

import java.util.concurrent.Future
import java.util.{HashMap, Properties}

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}
import org.apache.spark.sql.DataFrame

import scala.io.Source

object Producer {

  var producer: KafkaProducer[String,String] = _

  def main(args: Array[String]): Unit = {

    /*if(args.length < 4) {
      System.err.println("Usage: KafkaProducer <BrokerList> <topic> <messgaePerSec> <wordsPermessage>")
      System.exit(1)
    }*/

    Console.println("Started Producer")
    println("Producer started........")
    ConfigProvider.setConfig("/kafkaConfig.properties")

    val props = new Properties()

    println("Setting Props")

    props.put("bootstrap.servers", "localhost:9092")
    props.put("acks", "all")
    //props.put("retries", 1)
    //props.put("batch.size", 10)
    //props.put("linger.ms", 1)
    //props.put("buffer.memory", 33554432)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    producer = new KafkaProducer[String,String](props)

//Reading json files from Data folder

    //val businessDF = Source.fromFile("")
    //val userDF = DataLoader.getData("Data/json/yelp_academic_dataset_user.json")

    while(true){
      try{

        for(line <- Source.fromFile("Data/json/business.json").getLines()){
          produce("topicName",line)
        }
        Thread.sleep(1000)
      } catch {
        case e:Exception => e.printStackTrace()
        producer.close()
      }
      println("Done Producing message")
    }
  }

  def produce(topic:String,data:String): Future[RecordMetadata] ={

    producer.send(new ProducerRecord[String,String](topic,data))
  }
}
