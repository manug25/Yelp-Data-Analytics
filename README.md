[![Travis Build Status](https://travis-ci.org/manug25/Yelp-Data-Analytics.svg?branch=master)](https://travis-ci.org/manug25/Yelp-Data-Analytics)
# Yelp-Data-Analytics
The Project is to stream Yelp data and store it in Hbase table as well as find some insights.

Step1: Start zookeeper server.

Step2: Start kafka-server

Step3: To create topics execute below shell script

./createKafkaTopic.sh localhost:2181 1 1 businessTopic

Step4: Start Consumer application

Step5: Start Producer application
