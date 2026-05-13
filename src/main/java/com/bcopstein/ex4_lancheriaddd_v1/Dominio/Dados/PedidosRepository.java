package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import java.time.LocalDateTime;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public interface PedidosRepository {
    Pedido salva(Pedido pedido, String enderecoEntrega);
    Pedido.Status recuperaStatusPorId(long idPedido);
    int contaPedidosClienteDesde(String cpfCliente, LocalDateTime dataInicial);
}
