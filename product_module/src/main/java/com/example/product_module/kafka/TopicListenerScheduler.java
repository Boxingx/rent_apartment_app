package com.example.product_module.kafka;

import com.example.product_module.controller.ProductModuleController;
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

    private final ProductModuleController productModuleController;


    @Scheduled(fixedDelay = 30_000)
    public void topicsReader() {

        List<String> messages = consumerListener.getMessages();

        //todo хардкод убрать
        if (nonNull(messages)) {
            for (String message : messages) {
                productModuleController.addProductForBooking(Long.parseLong(message), "123weather123");
            }
            messages.clear();
        }
    }
}
