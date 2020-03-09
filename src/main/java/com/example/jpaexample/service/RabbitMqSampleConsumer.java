package com.example.jpaexample.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqSampleConsumer {

    @RabbitListener(queues = "${sample.chaitra.rabbitmq.queue}")
    public void recievedMessage(String incomingMsg) {
        System.out.println("Recieved Message From Sample RabbitMQ: " + incomingMsg);
    }
}
