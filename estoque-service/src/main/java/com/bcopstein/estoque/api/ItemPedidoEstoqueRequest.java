package com.bcopstein.estoque.api;

import java.util.List;

public record ItemPedidoEstoqueRequest(long produtoId, List<IngredienteQuantidadeRequest> ingredientes) {
}
