package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EntregaService implements IEntregaService {
    private RabbitTemplate rabbitTemplate;
    private String queueName;

    public EntregaService(RabbitTemplate rabbitTemplate,
                          @Value("${pizzaria.entregas.queue:pizzaria.entregas}") String queueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
    }

    @Override
    public void chegadaDePedido(long idPedido) {
        rabbitTemplate.convertAndSend(queueName, Long.toString(idPedido));
        System.out.println("Pedido " + idPedido + " enviado para a fila de entregas");
    }
}
