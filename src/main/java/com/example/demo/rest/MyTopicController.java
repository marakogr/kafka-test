package com.example.demo.rest;

import com.example.demo.kafka.prod.MyTopicProducer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
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
