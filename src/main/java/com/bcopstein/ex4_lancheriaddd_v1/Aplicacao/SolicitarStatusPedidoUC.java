package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.StatusPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Component
public class SolicitarStatusPedidoUC {
    private PedidosRepository pedidosRepository;

    public SolicitarStatusPedidoUC(PedidosRepository pedidosRepository) {
        this.pedidosRepository = pedidosRepository;
    }

    public StatusPedidoResponse run(long idPedido) {
        Pedido.Status status = pedidosRepository.recuperaStatusPorId(idPedido);
        if (status == null) {
            throw new IllegalArgumentException("Pedido nao encontrado: " + idPedido);
        }
        return new StatusPedidoResponse(idPedido, status);
    }
}
