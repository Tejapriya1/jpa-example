package com.example.jpaexample.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.example.jpaexample.model.Employee;

public class QueueService {
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Value("${unni.rabbitmq.exchange}")
	private String exchange;
	
	@Value("${unni.rabbitmq.addkey}")
	private String addkey;	
	
	@Value("${unni.rabbitmq.deletekey}")
	private String deletekey;	

	
	public void addSend(Employee company) {
		amqpTemplate.convertAndSend(exchange, addkey, company);
		System.out.println("Send msg = " + company);
	    
	}
	public void deleteSend(Employee company) {
		amqpTemplate.convertAndSend(exchange, deletekey, company);
		System.out.println("Send msg = " + company);
	    
	}

}
