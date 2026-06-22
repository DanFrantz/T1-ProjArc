package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.CardapioCorrenteRepository;

@Component
public class CardapioCorrenteRepositoryFake implements CardapioCorrenteRepository {
    private static final String CHAVE = "CARDAPIO_CORRENTE";
    private JdbcTemplate jdbcTemplate;

    public CardapioCorrenteRepositoryFake(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long recuperaIdCardapioCorrente() {
        String valor = jdbcTemplate.queryForObject(
            "SELECT valor FROM configuracoes WHERE chave = ?",
            String.class,
            CHAVE);
        return Long.parseLong(valor);
    }

    @Override
    public void defineIdCardapioCorrente(long idCardapio) {
        jdbcTemplate.update(
            "UPDATE configuracoes SET valor = ? WHERE chave = ?",
            Long.toString(idCardapio),
            CHAVE);
    }
}
