package com.bcopstein.estoque.api;

import java.util.List;

public record VerificacaoEstoqueResponse(List<Long> produtosSemEstoque) {
}
