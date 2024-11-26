package com.marakogr.kafka;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class KafkaPropertiesProvider {

    protected final BeanFactory beanFactory;
    @Value("${kafka.group_id.prefix.generation}")
    private boolean isGeneratorEnabled;

    protected KafkaPropertiesProvider(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    protected abstract class PropertiesInstance {

        private final Map<String, String> cache = new HashMap<>();

        protected String resolve(String expression) {
            return resolve(expression, true);
        }

        /**
         * Resolves provided SpEL expression, that should
         * contain only ${} embedded values, since used only
         * {@link ConfigurableBeanFactory#resolveEmbeddedValue(java.lang.String)}.
         *
         * @param expression - SpEL expression only with ${}
         * @param saveInCache - boolean that says
         *                    whether we should save value in cache
         * @return
         */
        protected String resolve(String expression, boolean saveInCache) {
            if (containsInCache(expression)) {
                return getFromCache(expression);
            }

            String resolvedValue;
            if (beanFactory != null && beanFactory instanceof ConfigurableBeanFactory) {
                resolvedValue = ((ConfigurableBeanFactory) beanFactory).resolveEmbeddedValue(expression);
            } else {
                resolvedValue = expression;
            }

            if (saveInCache) {
                addInCache(expression, resolvedValue);
            }
            return resolvedValue;
        }

        protected String addInCache(String key, String value) {
            cache.put(key, value);
            return value;
        }

        protected boolean containsInCache(String key) {
            return cache.containsKey(key);
        }

        protected String getFromCache(String key) {
            return cache.get(key);
        }

        protected boolean isAutoGeneratorEnabled() {
            return isGeneratorEnabled;
        }

        protected String getGeneratedValue(String property) {
            String propertyValue;
            if (containsInCache(property)) {
                propertyValue = getFromCache(property);
            } else {
                String randomValue = generateRandomValue();
                propertyValue = addInCache(property, randomValue);
            }
            return propertyValue;
        }

        protected String generateRandomValue() {
            return UUID.randomUUID().toString();
        }
    }
}
