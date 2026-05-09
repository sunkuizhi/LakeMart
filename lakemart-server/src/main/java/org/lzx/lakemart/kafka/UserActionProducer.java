package org.lzx.lakemart.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserActionProducer {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "user-actions";

    public void sendAction(Object action) {
        kafkaTemplate.send(TOPIC, action);
    }
}