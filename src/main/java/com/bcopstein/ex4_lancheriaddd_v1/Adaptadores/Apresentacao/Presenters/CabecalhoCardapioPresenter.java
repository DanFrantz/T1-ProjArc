package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.CabecalhoCardapio;

public record CabecalhoCardapioPresenter(long id,String titulo) {
    public static CabecalhoCardapioPresenter from(CabecalhoCardapio cabecalhoCardapio) {
        return new CabecalhoCardapioPresenter(cabecalhoCardapio.id(), cabecalhoCardapio.titulo());
    }
}
