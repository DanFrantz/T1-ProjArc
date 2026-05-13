package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public record StatusPedidoResponse(long idPedido, String status) {
    public StatusPedidoResponse(long idPedido, Pedido.Status status) {
        this(idPedido, status.name());
    }
}
