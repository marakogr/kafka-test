package com.example.demo.kafka.prod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("kafka")
public class MyTopicProducer {
    //topic and group provider
    //logging callback for sent and receive

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public MyTopicProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        kafkaTemplate.send("my-topic", message);
    }
}
