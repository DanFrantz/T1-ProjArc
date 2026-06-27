package com.bcopstein.estoque.api;

import java.util.List;

public record MovimentacaoEstoqueRequest(List<IngredienteQuantidadeRequest> ingredientes) {
}
