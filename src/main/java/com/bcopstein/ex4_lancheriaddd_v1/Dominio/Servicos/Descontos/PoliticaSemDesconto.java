package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.Descontos;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PoliticaDesconto;

@Component
public class PoliticaSemDesconto implements PoliticaDesconto {
    @Override
    public String codigo() {
        return "SEM_DESCONTO";
    }

    @Override
    public String descricao() {
        return "Sem desconto";
    }

    @Override
    public double calcula(Cliente cliente, double valorItens) {
        return 0;
    }
}
