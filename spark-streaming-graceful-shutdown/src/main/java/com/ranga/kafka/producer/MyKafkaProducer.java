package com.ranga.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class MyKafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyKafkaProducer.class);

    public static void main(String[] args) throws Exception {

        if (args.length < 2) {
            LOGGER.error("Usage   : MyKafkaProducer <bootstrap_servers> <topic_name>");
            LOGGER.info("Example : MyKafkaProducer localhost:9092 test_topic");
            return;
        }

        String bootstrapServers = args[0];
        String topicName = args[1];

        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        long count = 0;
        List<String> lines = Files.readAllLines(Paths.get("README.md"));
        for (String line : lines) {
            ProducerRecord<String, String> record = new ProducerRecord<>(topicName, String.valueOf(count++), line);
            producer.send(record);
            LOGGER.debug("Message sent successfully");
            if( count % 50 == 0) {
                Thread.sleep(20 * 1000);
            }
        }
        LOGGER.info("All Messages to Kafka successfully");
        producer.close();
    }
}