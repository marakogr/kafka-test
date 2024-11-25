package com.example.demo.kafka.cons;

import com.example.demo.kafka.KafkaLogUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Profile("kafka")
public class MyTopicConsumer {

    @KafkaListener(topics = "my-topic", groupId = "test-group")
    public void listen(@Payload String message, @Headers MessageHeaders headers) {
        KafkaLogUtils.logReceived(message, headers, "my-topic-event");
    }
}
