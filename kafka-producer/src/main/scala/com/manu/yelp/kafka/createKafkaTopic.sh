#!/bin/bash
#
#Shell script for creating Kafka topic
#Change export PATH, add your kafka /bin directory
#$1: Zookeeper configuration
#$2: replication-factor
#$3: Number of partitions
#$4: Topic name
#

#if [ "$#" < 4 ]; then
#echo "Incorrect number of arguments passed"
echo "Usage: kafka-topic.sh --create <zookeeper config> <replication-factor> <number of partitions> <topic name>"
#else
kafka-topics.sh --create --zookeeper $1 --replication-factor $2 --partitions $3 --topic $4
#fi
