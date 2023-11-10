package com.example.product_module.kafka;

import com.example.product_module.controller.ProductModelController;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.nonNull;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class TopicListenerScheduler {


    private final ConsumerListener consumerListener;

    private final ProductModelController productModelController;


    @Scheduled(fixedDelay = 60_000)
    public void topicsReader() {

        List<String> messages = consumerListener.getMessages();

        if (nonNull(messages)) {
            for (String message : messages) {
                productModelController.addProductForBooking(Long.parseLong(message), null);
            }
        }
    }
}
