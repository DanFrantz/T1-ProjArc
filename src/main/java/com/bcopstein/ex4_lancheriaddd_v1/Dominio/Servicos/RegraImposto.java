package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

public interface RegraImposto {
    String lei();
    String descricao();
    double calcula(double valorItens);
}
