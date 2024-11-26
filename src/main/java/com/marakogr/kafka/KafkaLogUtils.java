package com.marakogr.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.MessageHeaders;

import java.util.UUID;

public final class KafkaLogUtils {

    private static final Logger CONSUMERS_LOG = LoggerFactory.getLogger(KafkaConstants.KAFKA_LISTENERS_LOGGER_NAME);
    private static final Logger PRODUCERS_LOG = LoggerFactory.getLogger(KafkaConstants.KAFKA_PRODUCERS_LOGGER_NAME);

    private static final String NO_VALUE = "No value";

    private KafkaLogUtils() {
    }

    public static String generateInternalId() {
        return UUID.randomUUID().toString();
    }

    public static void logReceived(Object event, MessageHeaders headers, String eventName) {
        logReceived(event, headers, eventName, generateInternalId());
    }

    public static void logReceived(Object event, MessageHeaders headers, String eventName, String internalEventId) {
        Object topic = headers.get(KafkaHeaders.RECEIVED_TOPIC);
        Object partition = headers.get(KafkaHeaders.RECEIVED_PARTITION);
        Object offset = headers.get(KafkaHeaders.OFFSET);

        CONSUMERS_LOG.info("Received {} event. InternalId: {}. Topic: {}. Partition: {}. Offset: {}",
                eventName, internalEventId, topic, partition, offset);
        CONSUMERS_LOG.debug("[{}] Event body: {}", internalEventId, event);
    }

    public static void logAck(String internalEventId) {
        CONSUMERS_LOG.info("[{}] Event has been acknowledged", internalEventId);
    }

    public static void logNack(String internalEventId) {
        CONSUMERS_LOG.info("[{}] Event has been rejected with nack", internalEventId);
    }

    public static void logSuccessfulSent(SendResult<?, ?> result, String eventName) {
        RecordMetadata metadata = result.getRecordMetadata();
        if (PRODUCERS_LOG.isDebugEnabled()) {
            ProducerRecord<?, ?> record = result.getProducerRecord();
            PRODUCERS_LOG.debug("Has sent {} event. Topic: {}. Partition: {}. Offset: {}\nKey: {}\nValue: {}",
                    eventName, metadata.topic(), metadata.partition(), metadata.offset(),
                    convertToString(record.key()), convertToString(record.value()));
        } else {
            PRODUCERS_LOG.info("Has sent {} event. Topic: {}. Partition: {}. Offset: {}",
                    eventName, metadata.topic(), metadata.partition(), metadata.offset());
        }
    }

    public static void logFailedSent(Throwable ex, String eventName, String topic, Object key, Object value) {
        if (PRODUCERS_LOG.isDebugEnabled()) {
            PRODUCERS_LOG.error("Has failed to send {} event. Topic: {}\nKey: {}\nValue: {}",
                    eventName, topic, convertToString(key), convertToString(value), ex);
        } else {
            PRODUCERS_LOG.error("Has failed to send {} event. Topic: {}", eventName, topic, ex);
        }
    }

    private static String convertToString(Object obj) {
        if (obj == null) {
            return NO_VALUE;
        }

        return obj.toString();
    }
}
