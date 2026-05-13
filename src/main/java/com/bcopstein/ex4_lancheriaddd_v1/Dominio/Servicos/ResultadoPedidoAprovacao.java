package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

public record ResultadoPedidoAprovacao(Pedido pedido, List<Produto> produtosIndisponiveis) {
    public boolean aprovado() {
        return produtosIndisponiveis.isEmpty();
    }
}
