package com.example.demo.kafka;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component("topicAndGroupProvider")
public class TopicAndGroupProvider extends KafkaPropertiesProvider {

    private static final String GROUP = "group";
    private final Set<String> topics;

    private TopicAndGroup my;

    public TopicAndGroupProvider(BeanFactory beanFactory) {
        super(beanFactory);
        this.topics = new HashSet<>();
    }

    @PostConstruct
    private void afterPropertiesSet() {
        my = cache(new TopicAndGroup() {
            @Override
            public String topic(String... additionalInfo) {
                return resolve("${kafka.topics.my}");
            }

            @Override
            public String group() {
                return isAutoGeneratorEnabled()
                        ? getGeneratedValue(GROUP)
                        : resolve("${kafka.groups.my}");
            }
        });
    }

    public Set<String> getAllTopics() {
        return Collections.unmodifiableSet(topics);
    }

    public TopicAndGroup my() {
        return my;
    }

    public abstract class TopicAndGroup extends PropertiesInstance {

        public abstract String topic(String... additionalInfo);

        public String group() {
            return null;
        }
    }

    private TopicAndGroup cache(TopicAndGroup instance) {
        if (instance != null) {
            topics.add(instance.topic());
        }
        return instance;
    }
}
