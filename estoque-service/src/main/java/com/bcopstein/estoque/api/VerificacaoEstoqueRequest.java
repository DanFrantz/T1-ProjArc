package com.bcopstein.estoque.api;

import java.util.List;

public record VerificacaoEstoqueRequest(List<ItemPedidoEstoqueRequest> itens) {
}
