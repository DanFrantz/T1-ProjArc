package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.Impostos;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.RegraImposto;

@Component
public class RegraImpostoLei10 implements RegraImposto {
    @Override
    public String lei() {
        return "LEI_10";
    }

    @Override
    public String descricao() {
        return "Imposto unico de 10% sobre o valor dos itens";
    }

    @Override
    public double calcula(double valorItens) {
        return valorItens * 0.10;
    }
}
