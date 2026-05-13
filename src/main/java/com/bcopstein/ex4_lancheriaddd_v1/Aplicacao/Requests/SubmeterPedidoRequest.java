package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests;

import java.util.List;

public record SubmeterPedidoRequest(String emailCliente, String enderecoEntrega, List<ItemPedidoRequest> itens) {
    public record ItemPedidoRequest(long produtoId, int quantidade) {
    }
}
