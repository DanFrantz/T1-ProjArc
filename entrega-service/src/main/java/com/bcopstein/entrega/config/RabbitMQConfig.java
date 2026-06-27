package com.bcopstein.entrega.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue entregasQueue(@Value("${pizzaria.entregas.queue}") String queueName) {
        return new Queue(queueName, true);
    }
}
