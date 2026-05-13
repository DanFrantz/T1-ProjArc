package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

@Service
public class EstoqueService {
    private JdbcTemplate jdbcTemplate;

    public EstoqueService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Produto> produtosSemEstoque(List<ItemPedido> itens) {
        Map<Long, Integer> estoqueDisponivel = recuperaEstoqueDisponivel();

        return itens.stream()
            .filter(item -> !reservaSePossivel(item, estoqueDisponivel))
            .map(ItemPedido::getItem)
            .toList();
    }

    public void baixaIngredientes(List<ItemPedido> itens) {
        Map<Long, Integer> ingredientesNecessarios = new HashMap<>();
        for (ItemPedido item : itens) {
            calculaIngredientesNecessarios(item)
                .forEach((ingredienteId, quantidade) ->
                    ingredientesNecessarios.merge(ingredienteId, quantidade, Integer::sum));
        }

        ingredientesNecessarios.forEach((ingredienteId, quantidade) ->
            jdbcTemplate.update(
                "UPDATE itensEstoque SET quantidade = quantidade - ? WHERE ingrediente_id = ?",
                quantidade,
                ingredienteId));
    }

    public void devolveIngredientes(List<ItemPedido> itens) {
        Map<Long, Integer> ingredientesNecessarios = new HashMap<>();
        for (ItemPedido item : itens) {
            calculaIngredientesNecessarios(item)
                .forEach((ingredienteId, quantidade) ->
                    ingredientesNecessarios.merge(ingredienteId, quantidade, Integer::sum));
        }

        ingredientesNecessarios.forEach((ingredienteId, quantidade) ->
            jdbcTemplate.update(
                "UPDATE itensEstoque SET quantidade = quantidade + ? WHERE ingrediente_id = ?",
                quantidade,
                ingredienteId));
    }

    private Map<Long, Integer> recuperaEstoqueDisponivel() {
        String sql = "SELECT ingrediente_id, quantidade FROM itensEstoque";
        Map<Long, Integer> estoque = new HashMap<>();
        jdbcTemplate.query(
            sql,
            ps -> {},
            rs -> {
                estoque.put(rs.getLong("ingrediente_id"), rs.getInt("quantidade"));
            });
        return estoque;
    }

    private boolean reservaSePossivel(ItemPedido itemPedido, Map<Long, Integer> estoqueDisponivel) {
        Map<Long, Integer> ingredientesNecessarios = calculaIngredientesNecessarios(itemPedido);
        boolean temEstoque = ingredientesNecessarios.entrySet().stream()
            .allMatch(ingrediente -> estoqueDisponivel.getOrDefault(ingrediente.getKey(), 0) >= ingrediente.getValue());

        if (temEstoque) {
            ingredientesNecessarios.forEach((ingredienteId, quantidade) ->
                estoqueDisponivel.put(ingredienteId, estoqueDisponivel.get(ingredienteId) - quantidade));
        }
        return temEstoque;
    }

    private Map<Long, Integer> calculaIngredientesNecessarios(ItemPedido itemPedido) {
        Map<Long, Integer> ingredientesNecessarios = new HashMap<>();
        for (Ingrediente ingrediente : itemPedido.getItem().getReceita().getIngredientes()) {
            ingredientesNecessarios.merge(ingrediente.getId(), itemPedido.getQuantidade(), Integer::sum);
        }
        return ingredientesNecessarios;
    }
}
