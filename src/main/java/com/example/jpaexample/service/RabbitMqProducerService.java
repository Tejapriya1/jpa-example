package com.example.jpaexample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqProducerService {
    @Autowired
    RabbitMQSender rabbitMQSender;

        public void mqProducer(String body){
        rabbitMQSender.send(body);

    }
}
