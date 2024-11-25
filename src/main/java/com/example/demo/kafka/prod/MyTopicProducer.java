package com.example.demo.kafka.prod;

import com.example.demo.kafka.ProducerCallbackAppender;
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

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ProducerCallbackAppender callbackAppender;

    public MyTopicProducer(KafkaTemplate<String, String> kafkaTemplate, ProducerCallbackAppender callbackAppender) {
        this.kafkaTemplate = kafkaTemplate;
        this.callbackAppender = callbackAppender;
    }

    public void sendMessage(String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC, message);
        callbackAppender.addCallbacks("my-topic-event", message, TOPIC, future);
    }
}
