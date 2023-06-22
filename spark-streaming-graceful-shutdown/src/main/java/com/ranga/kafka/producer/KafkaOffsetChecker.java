package com.ranga.kafka.producer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class KafkaOffsetChecker {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaOffsetChecker.class);

    public static void main(String[] args) {

        if (args.length < 3) {
            LOGGER.error("Usage  : KafkaOffsetChecker <bootstrap_servers> <topic_name> <group_name>");
            LOGGER.info("Example : KafkaOffsetChecker localhost:9092 test_topic test_group");
            return;
        }

        // Set Kafka broker address and topic name
        String bootstrapServers = args[0];
        String topicName = args[1];
        String groupName = args[2];

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupName);

        try (KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props)) {
            Map<String, List<PartitionInfo>> topics = kafkaConsumer.listTopics();
            if (topics.containsKey(topicName)) {
                List<PartitionInfo> partitionInfoList = topics.get(topicName);

                // Iterate over the partitions and get the offset information
                for (PartitionInfo partitionInfo : partitionInfoList) {
                    int partitionId = partitionInfo.partition();
                    TopicPartition topicPartition = new TopicPartition(topicName, partitionId);
                    Set<TopicPartition> topicPartitionSet = Collections.singleton(topicPartition);
                    // Assign the partition to the consumer
                    kafkaConsumer.assign(Collections.singleton(topicPartition));

                    Map<TopicPartition, Long> beginningOffsets = kafkaConsumer.beginningOffsets(topicPartitionSet);
                    Map<TopicPartition, Long> endOffsets = kafkaConsumer.endOffsets(topicPartitionSet);
                    long beginOffset = beginningOffsets.entrySet().iterator().next().getValue();
                    long endOffset = endOffsets.entrySet().iterator().next().getValue();
                    long currentOffset = kafkaConsumer.position(topicPartition);
                    long lag = endOffset - currentOffset;
                    String offsetInfo = String.format("Topic: <%s>, Partition: <%d>, BeginOffset: <%d>, EndOffset: <%d>, CurrentOffset: <%d>, Log: <%d>",
                            topicName, partitionId, beginOffset, endOffset, currentOffset, lag);
                    System.out.println(offsetInfo);
                    LOGGER.info(offsetInfo);
                }
            } else {
                LOGGER.error("Topic {} does not exist", topicName);
            }
        }
    }
}
