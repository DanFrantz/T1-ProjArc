package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import java.util.Map;

public interface EstoqueRepository {
    Map<Long, Integer> recuperaEstoqueDisponivel();
    void baixaIngrediente(long ingredienteId, int quantidade);
    void devolveIngrediente(long ingredienteId, int quantidade);
}
