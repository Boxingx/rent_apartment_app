package com.example.rent_module.kafka;

public interface KafkaProducer {

    String sendMessageToTopic(String message);
}
