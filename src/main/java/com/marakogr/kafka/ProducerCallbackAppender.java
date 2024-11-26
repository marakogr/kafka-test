package com.marakogr.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Profile("kafka")
public class ProducerCallbackAppender {

    @Value("${kafka.logging.producers.enabled}")
    private Boolean kafkaLoggingEnabled;

    public <V> void addCallbacks(String eventName, V value, String topic,
                                 CompletableFuture<SendResult<String, V>> future) {
        if (kafkaLoggingEnabled) {
            future.whenComplete(new KafkaLoggingFutureCallback<>(null, value, eventName, topic));
        }
    }

    public <V> void addCallbacks(String eventName, String key, V value, String topic,
                                 CompletableFuture<SendResult<String, V>> future) {
        if (kafkaLoggingEnabled) {
            future.whenComplete(new KafkaLoggingFutureCallback<>(key, value, eventName, topic));
        }
    }
}
