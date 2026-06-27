package com.bcopstein.estoque.aplicacao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcopstein.estoque.api.IngredienteQuantidadeRequest;
import com.bcopstein.estoque.api.ItemPedidoEstoqueRequest;
import com.bcopstein.estoque.api.MovimentacaoEstoqueRequest;
import com.bcopstein.estoque.api.VerificacaoEstoqueRequest;
import com.bcopstein.estoque.api.VerificacaoEstoqueResponse;
import com.bcopstein.estoque.dados.ItemEstoqueRepository;
import com.bcopstein.estoque.dominio.ItemEstoque;

@Service
public class EstoqueService {
    private ItemEstoqueRepository itemEstoqueRepository;

    public EstoqueService(ItemEstoqueRepository itemEstoqueRepository) {
        this.itemEstoqueRepository = itemEstoqueRepository;
    }

    public VerificacaoEstoqueResponse verifica(VerificacaoEstoqueRequest request) {
        Map<Long, Integer> estoqueSimulado = estoqueDisponivel();
        List<Long> produtosSemEstoque = new ArrayList<>();

        for (ItemPedidoEstoqueRequest item : request.itens()) {
            if (reservaSePossivel(item.ingredientes(), estoqueSimulado)) {
                continue;
            }
            produtosSemEstoque.add(item.produtoId());
        }

        return new VerificacaoEstoqueResponse(produtosSemEstoque);
    }

    @Transactional
    public void baixa(MovimentacaoEstoqueRequest request) {
        request.ingredientes().forEach(ingrediente -> {
            ItemEstoque item = recuperaItem(ingrediente.ingredienteId());
            if (item.getQuantidade() < ingrediente.quantidade()) {
                throw new IllegalStateException("Estoque insuficiente para ingrediente " + ingrediente.ingredienteId());
            }
            item.baixa(ingrediente.quantidade());
        });
    }

    @Transactional
    public void devolve(MovimentacaoEstoqueRequest request) {
        request.ingredientes().forEach(ingrediente -> {
            ItemEstoque item = recuperaItem(ingrediente.ingredienteId());
            item.devolve(ingrediente.quantidade());
        });
    }

    private Map<Long, Integer> estoqueDisponivel() {
        Map<Long, Integer> estoque = new HashMap<>();
        itemEstoqueRepository.findAll().forEach(item ->
            estoque.put(item.getIngrediente().getId(), item.getQuantidade()));
        return estoque;
    }

    private boolean reservaSePossivel(List<IngredienteQuantidadeRequest> ingredientes,
                                      Map<Long, Integer> estoqueSimulado) {
        boolean temEstoque = ingredientes.stream()
            .allMatch(ingrediente ->
                estoqueSimulado.getOrDefault(ingrediente.ingredienteId(), 0) >= ingrediente.quantidade());

        if (temEstoque) {
            ingredientes.forEach(ingrediente ->
                estoqueSimulado.put(
                    ingrediente.ingredienteId(),
                    estoqueSimulado.get(ingrediente.ingredienteId()) - ingrediente.quantidade()));
        }
        return temEstoque;
    }

    private ItemEstoque recuperaItem(long ingredienteId) {
        return itemEstoqueRepository.findByIngredienteId(ingredienteId)
            .orElseThrow(() -> new IllegalArgumentException("Ingrediente sem estoque: " + ingredienteId));
    }
}
