package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;
 
import java.util.List;
 
public record PedidosEntreguesResponse(List<PedidoEntregueResponse> pedidos) {
 
    public record PedidoEntregueResponse(
        long id,
        String cpfCliente,
        String nomeCliente,
        String dataHoraPagamento,
        double valor,
        double impostos,
        double desconto,
        double valorCobrado,
        List<ItemPedidoResponse> itens) {
 
        public record ItemPedidoResponse(String descricao, int quantidade) {}
    }
}