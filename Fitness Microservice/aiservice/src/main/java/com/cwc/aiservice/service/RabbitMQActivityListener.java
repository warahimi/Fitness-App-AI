package com.cwc.aiservice.service;

import com.cwc.aiservice.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQActivityListener {
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void processActivityMessage(Activity activity) {
        log.info("Processing activity message from RabbitMQ");

        log.info("Received activity message: {}", activity);
        // Here you can add logic to process the activity message
        // For example, you might want to save it to a database or perform some business logic

        log.info("Activity message processed successfully");
    }
}
