package com.marakogr.kafka.prod;

import com.marakogr.kafka.ProducerCallbackAppender;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Profile("kafka")
public class MyTopicProducer {
    public static final String TOPIC = "my-topic";
    //topic and group provider
    //logging callback for sent and receive

    private final KafkaTemplate<String, JsonNode> kafkaTemplate;
    private final ProducerCallbackAppender callbackAppender;

    public MyTopicProducer(KafkaTemplate<String, JsonNode> kafkaTemplate, ProducerCallbackAppender callbackAppender) {
        this.kafkaTemplate = kafkaTemplate;
        this.callbackAppender = callbackAppender;
    }

    public void sendMessage(JsonNode message) {
        CompletableFuture<SendResult<String, JsonNode>> future = kafkaTemplate.send(TOPIC, message);
        callbackAppender.addCallbacks("my-topic-event", message, TOPIC, future);
    }
}
