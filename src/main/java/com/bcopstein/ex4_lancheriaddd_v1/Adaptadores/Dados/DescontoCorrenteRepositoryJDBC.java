package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.DescontoCorrenteRepository;

@Component
public class DescontoCorrenteRepositoryJDBC implements DescontoCorrenteRepository {
    private static final String CHAVE = "DESCONTO_CORRENTE";
    private JdbcTemplate jdbcTemplate;

    public DescontoCorrenteRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String recuperaCodigoDescontoCorrente() {
        return jdbcTemplate.queryForObject(
            "SELECT valor FROM configuracoes WHERE chave = ?",
            String.class,
            CHAVE);
    }

    @Override
    public void defineCodigoDescontoCorrente(String codigo) {
        jdbcTemplate.update(
            "UPDATE configuracoes SET valor = ? WHERE chave = ?",
            codigo,
            CHAVE);
    }
}
