package com.example.jpaexample.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

        @Value("${chaitra.rabbitmq.queue}")
        String queueNameChaitra;

        @Value("${sample.chaitra.rabbitmq.queue}")
        String queueNameSample;

        @Value("${chaitra.rabbitmq.routingkey.sample}")
        private String routingkeySample;

        @Value("${chaitra.rabbitmq.routingkey}")
        private String routingkey;

        @Value("${chaitra.topic.rabbitmq.exchange}")
        String exchange;



        @Bean
        Queue chaitraQueue() {
        return new Queue(queueNameChaitra, false);
    }

        @Bean
        Queue sampleQueue() {
        return new Queue(queueNameSample, false);
    }

        @Bean
        TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

        @Bean
        Binding bindingChaitra(TopicExchange exchange) {
        return BindingBuilder.bind(chaitraQueue()).to(exchange).with(routingkey);
   }
        @Bean
         Binding bindingSample(TopicExchange exchange) {
        return BindingBuilder.bind(sampleQueue()).to(exchange).with(routingkeySample);
    }

        @Bean
        public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

        public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

}
