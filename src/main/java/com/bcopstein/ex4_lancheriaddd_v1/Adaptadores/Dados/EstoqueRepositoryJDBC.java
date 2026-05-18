package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.EstoqueRepository;

@Component
public class EstoqueRepositoryJDBC implements EstoqueRepository {
    private JdbcTemplate jdbcTemplate;

    public EstoqueRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Long, Integer> recuperaEstoqueDisponivel() {
        String sql = "SELECT ingrediente_id, quantidade FROM itensEstoque";
        Map<Long, Integer> estoque = new HashMap<>();
        jdbcTemplate.query(
            sql,
            rs -> {
                estoque.put(rs.getLong("ingrediente_id"), rs.getInt("quantidade"));
            });
        return estoque;
    }

    @Override
    public void baixaIngrediente(long ingredienteId, int quantidade) {
        jdbcTemplate.update(
            "UPDATE itensEstoque SET quantidade = quantidade - ? WHERE ingrediente_id = ?",
            quantidade,
            ingredienteId);
    }

    @Override
    public void devolveIngrediente(long ingredienteId, int quantidade) {
        jdbcTemplate.update(
            "UPDATE itensEstoque SET quantidade = quantidade + ? WHERE ingrediente_id = ?",
            quantidade,
            ingredienteId);
    }
}
