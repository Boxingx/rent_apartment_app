package com.example.product_module.kafka;

import lombok.Getter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@Getter
public class ConsumerListener {

    private final List<String> messages = new ArrayList<>();


    @KafkaListener(topics = "myTopic2", groupId = "UNIQUE_ID")
    public void listen(String message) {
        synchronized (messages) {
            messages.add(message);
        }
    }
}
