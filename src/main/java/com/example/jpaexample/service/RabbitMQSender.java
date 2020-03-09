package com.example.jpaexample.service;


import com.example.jpaexample.model.Employee;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Value("${chaitra.topic.rabbitmq.exchange}")
	private String exchange;
	
	@Value("${chaitra.rabbitmq.routingkey}")
	private String routingkey;	

	
	public void send(String company) {
		amqpTemplate.convertAndSend(exchange, routingkey, company);
		System.out.println("Send msg = " + company);
	    
	}
}