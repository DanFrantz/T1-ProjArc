package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.Descontos;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PoliticaDesconto;

@Component
public class PoliticaPromocao10 implements PoliticaDesconto {
    @Override
    public String codigo() {
        return "PROMOCAO_10";
    }

    @Override
    public String descricao() {
        return "Promocao geral de 10%";
    }

    @Override
    public double calcula(Cliente cliente, double valorItens) {
        return valorItens * 0.10;
    }
}
