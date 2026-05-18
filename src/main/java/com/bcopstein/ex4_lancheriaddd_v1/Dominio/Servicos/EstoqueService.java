package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.EstoqueRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

@Service
public class EstoqueService {
    private EstoqueRepository estoqueRepository;

    public EstoqueService(EstoqueRepository estoqueRepository) {
        this.estoqueRepository = estoqueRepository;
    }

    public List<Produto> produtosSemEstoque(List<ItemPedido> itens) {
        Map<Long, Integer> estoqueDisponivel = estoqueRepository.recuperaEstoqueDisponivel();

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
            estoqueRepository.baixaIngrediente(ingredienteId, quantidade));
    }

    public void devolveIngredientes(List<ItemPedido> itens) {
        Map<Long, Integer> ingredientesNecessarios = new HashMap<>();
        for (ItemPedido item : itens) {
            calculaIngredientesNecessarios(item)
                .forEach((ingredienteId, quantidade) ->
                    ingredientesNecessarios.merge(ingredienteId, quantidade, Integer::sum));
        }

        ingredientesNecessarios.forEach((ingredienteId, quantidade) ->
            estoqueRepository.devolveIngrediente(ingredienteId, quantidade));
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
