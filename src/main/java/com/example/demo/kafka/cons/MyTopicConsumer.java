package com.example.demo.kafka.cons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("kafka")
public class MyTopicConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MyTopicConsumer.class);

    @KafkaListener(topics = "my-topic", groupId = "test-group")
    public void listen(String message) {
        logger.info("Received message: " + message);
    }
}
