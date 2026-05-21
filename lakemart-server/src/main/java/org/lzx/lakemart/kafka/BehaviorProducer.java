package org.lzx.lakemart.kafka;

import org.lzx.lakemart.model.dto.BehaviorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BehaviorProducer {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "user-behaviors";

    public void sendBehavior(BehaviorMessage message) {
        kafkaTemplate.send(TOPIC, message);
    }
}