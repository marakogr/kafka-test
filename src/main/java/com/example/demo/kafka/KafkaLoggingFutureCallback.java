package com.example.demo.kafka;

import org.springframework.kafka.support.SendResult;

import java.util.function.BiConsumer;

public class KafkaLoggingFutureCallback<K, V> implements BiConsumer<SendResult<K, V>, Throwable> {

    private final K key;
    private final V value;
    private final String eventName;
    private final String topic;

    public KafkaLoggingFutureCallback(K key, V value, String eventName, String topic) {
        this.key = key;
        this.value = value;
        this.eventName = eventName;
        this.topic = topic;
    }

    @Override
    public void accept(SendResult<K, V> stringVSendResult, Throwable action) {
        if (action != null) {
            KafkaLogUtils.logFailedSent(action, eventName, topic, key, value);
        } else {
            KafkaLogUtils.logSuccessfulSent(stringVSendResult, eventName);
        }
    }
}
