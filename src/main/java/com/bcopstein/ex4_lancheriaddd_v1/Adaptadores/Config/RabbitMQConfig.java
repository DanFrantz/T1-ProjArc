package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue entregasQueue(@Value("${pizzaria.entregas.queue:pizzaria.entregas}") String queueName) {
        return new Queue(queueName, true);
    }
}
