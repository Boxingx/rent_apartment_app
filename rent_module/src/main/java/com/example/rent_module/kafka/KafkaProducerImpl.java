package com.example.rent_module.kafka;


import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class KafkaProducerImpl implements KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;


    public String sendMessageToTopic(String message) {
        kafkaTemplate.send("myTopic", message);
        return "Сообщение добавлено в топик";
    }
}
