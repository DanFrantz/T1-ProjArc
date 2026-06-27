package com.bcopstein.entrega.aplicacao;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.bcopstein.entrega.api.AtualizarStatusPedidoRequest;

@Component
public class EntregaConsumer {
    private RestClient pizzariaClient;

    public EntregaConsumer(RestClient.Builder restClientBuilder) {
        this.pizzariaClient = restClientBuilder.baseUrl("http://pizzaria-service").build();
    }

    @RabbitListener(queues = "${pizzaria.entregas.queue}")
    public void recebePedido(String mensagem) throws InterruptedException {
        Long idPedido = Long.parseLong(mensagem);
        System.out.println("Entrega recebeu pedido " + idPedido);
        Thread.sleep(5000);
        atualizaStatus(idPedido, "TRANSPORTE");
        Thread.sleep(10000);
        atualizaStatus(idPedido, "ENTREGUE");
        System.out.println("Entrega finalizou pedido " + idPedido);
    }

    private void atualizaStatus(Long idPedido, String status) {
        pizzariaClient.patch()
            .uri("/internal/pedidos/{id}/status", idPedido)
            .body(new AtualizarStatusPedidoRequest(status))
            .retrieve()
            .toBodilessEntity();
    }
}
