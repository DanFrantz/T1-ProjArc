package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

public interface PoliticaDesconto {
    String codigo();
    String descricao();
    double calcula(Cliente cliente, double valorItens);
}
