#!/bin/bash
#
#Shell script for producing data of files to Kafka topic
#Change export PATH, add your kafka-broker/bin directory
#$1: data file to be send, specify absolute path
#$2: Kafka broker-list
#$3: topic name
#
#
export PATH=$PATH:kafka-broker/bin directory
FILES=$1
for f in $FILES
do
    echo "pushing $f file"
    cat $f | kafka-console-producer.sh --broker-list $2  --topic $3
    sleep 60
done