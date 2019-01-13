[![Travis Build Status](https://travis-ci.org/manug25/Yelp-Data-Analytics.svg?branch=master)](https://travis-ci.org/manug25/Yelp-Data-Analytics)
# Yelp-Data-Analytics
The Project is to stream Yelp data and store it in Hbase table as well as find some insights.

# Step1: Start zookeeper server.
Start zookeeper server and make sure it's running fine.
# Step2: Start kafka-server
Start kafka server with your port configuration.
# Step3: To create topics execute below shell script
```./createKafkaTopic.sh <zookeeper-server> <replication-factor> <partitions> <TopicName>```
e.g
./[createKafkaTopic.sh](https://github.com/manug25/Yelp-Data-Analytics/blob/master/kafka/src/main/scala/com/manu/yelp/kafka/createKafkaTopic.sh) localhost:2181 1 1 businessTopic

# Step4: Start Consumer application
* Go to streaming module
* mvn clean install
* Run Stream app using spark-submit
```bin/spark-submit --class <class-name> --master <yarn-cluster> <jar file path>```
e.g
```bin/spark-submit --class StreamData --master local[4]/media/manu/Coding/Coding/Yelp-Image-Analytics/streaming/target/streaming-1.0-SNAPSHOT.jar```
# Step5: Start Producer application
* Go to kafka module
* Provide Kafka param in file kafkaConfig.properties present in resource directory
* mvn clean install
* Run using spark-submit
     
     *Producer app using spark-submit
     
     ```spark-submit --class Producer <jar-file-path>```
     
     *Producing data using producer.sh script 
     
     ```./producer.sh <file-path> <kafka-broker-list> <topic-name>```
     
     e.g
     
     ```./producer.sh /data/json/user.json localhost:9092 userTopic```