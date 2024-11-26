package com.marakogr.rest;

import com.marakogr.kafka.prod.MyTopicProducer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
@Profile("kafka")
public class MyTopicController {

    private final MyTopicProducer producer;

    public MyTopicController(MyTopicProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    public String sendMessage(@RequestBody JsonNode message) {
        producer.sendMessage(message);
        return "Message sent to Kafka: " + message;
    }
}
