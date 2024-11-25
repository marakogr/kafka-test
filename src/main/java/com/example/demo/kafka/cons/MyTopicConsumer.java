package com.example.demo.kafka.cons;

import com.example.demo.kafka.KafkaLogUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Component
@Profile("kafka")
public class MyTopicConsumer {

    @KafkaListener(
            topics = "#{@topicAndGroupProvider.my().topic()}",
            groupId = "#{@topicAndGroupProvider.my().group()}",
            containerFactory = "myKafkaListenerContainerFactory")
    public void listen(@Payload JsonNode message, @Headers MessageHeaders headers, Acknowledgment ack) {
        String id = KafkaLogUtils.generateInternalId();
        KafkaLogUtils.logReceived(message, headers, "my-topic-event", id);
        try {
            process();
        } catch (TimeoutException e) {
            ack.nack(Duration.ofSeconds(10L));
            KafkaLogUtils.logNack(id);
        }
        ack.acknowledge();
        KafkaLogUtils.logAck(id);
    }

    private void process() throws TimeoutException {

    }
}
