package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.CardapioCorrenteRepository;

@Component
public class CardapioCorrenteRepositoryFake implements CardapioCorrenteRepository {
    @Override
    public long recuperaIdCardapioCorrente() {
        return 1L;
    }
}
