package com.cwc.aiservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/*
rabbitmq.exchange.name=fitness-exchange
rabbitmq.queue.name=activity-queue
rabbitmq.routing.name=activity-tracking-routing
 */
@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.exchange.name}")
    private String activityExchangeName;
    @Value("${rabbitmq.routing.name}")
    private String activityRoutingName;
    @Value("${rabbitmq.queue.name}")
    private String activityQueueName;

    @Bean
    public Queue activityQueue() {
        return new Queue(activityQueueName, true);
    }
//    @Bean
//    public TopicExchange exchange() {
//        return new TopicExchange(activityExchangeName);
//    }
    @Bean
    public DirectExchange activityExchange() {
        return new DirectExchange(activityExchangeName);
    }
    @Bean
    public Binding binding(Queue activityQueue, DirectExchange activityExchange) {
        return BindingBuilder.bind(activityQueue).to(activityExchange).with(activityRoutingName);
    }
    // Convert the message/object into JSON
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
