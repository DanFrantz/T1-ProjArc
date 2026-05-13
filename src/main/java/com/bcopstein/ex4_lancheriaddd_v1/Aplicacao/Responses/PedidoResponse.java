package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ResultadoPedidoAprovacao;

public record PedidoResponse(
    long id,
    String status,
    double valor,
    double impostos,
    double desconto,
    double valorCobrado,
    List<ItemIndisponivelResponse> itensIndisponiveis) {

    public record ItemIndisponivelResponse(long id, String descricao) {
    }

    public PedidoResponse(Pedido pedido) {
        this(
            pedido.getId(),
            pedido.getStatus().name(),
            pedido.getValor(),
            pedido.getImpostos(),
            pedido.getDesconto(),
            pedido.getValorCobrado(),
            List.of());
    }

    public PedidoResponse(ResultadoPedidoAprovacao resultado) {
        this(
            resultado.pedido().getId(),
            resultado.pedido().getStatus().name(),
            resultado.pedido().getValor(),
            resultado.pedido().getImpostos(),
            resultado.pedido().getDesconto(),
            resultado.pedido().getValorCobrado(),
            resultado.produtosIndisponiveis().stream()
                .map(PedidoResponse::criaItemIndisponivel)
                .toList());
    }

    private static ItemIndisponivelResponse criaItemIndisponivel(Produto produto) {
        return new ItemIndisponivelResponse(produto.getId(), produto.getDescricao());
    }
}
