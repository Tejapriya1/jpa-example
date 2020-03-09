package com.example.jpaexample.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqConsumerService {
    @RabbitListener(queues = "${chaitra.rabbitmq.queue}")
    public void recievedMessage(String incomingMsg) {
        System.out.println("Recieved Message From RabbitMQ: " + incomingMsg);
    }
}
