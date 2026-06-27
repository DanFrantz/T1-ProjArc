package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

@Service
public class EstoqueService {
    private RestClient estoqueClient;

    public EstoqueService(RestClient.Builder restClientBuilder) {
        this.estoqueClient = restClientBuilder.baseUrl("http://estoque-service").build();
    }

    public List<Produto> produtosSemEstoque(List<ItemPedido> itens) {
        VerificacaoEstoqueRequest request = new VerificacaoEstoqueRequest(
            itens.stream()
                .map(item -> new ItemPedidoEstoqueRequest(
                    item.getItem().getId(),
                    toIngredientesRequest(calculaIngredientesNecessarios(item))))
                .toList());

        VerificacaoEstoqueResponse response = estoqueClient.post()
            .uri("/estoque/verificacao")
            .body(request)
            .retrieve()
            .body(VerificacaoEstoqueResponse.class);

        List<Long> idsSemEstoque = response == null ? List.of() : response.produtosSemEstoque();
        return itens.stream()
            .map(ItemPedido::getItem)
            .filter(produto -> idsSemEstoque.contains(produto.getId()))
            .toList();
    }

    public void baixaIngredientes(List<ItemPedido> itens) {
        estoqueClient.post()
            .uri("/estoque/baixa")
            .body(new MovimentacaoEstoqueRequest(toIngredientesRequest(calculaIngredientesNecessarios(itens))))
            .retrieve()
            .toBodilessEntity();
    }

    public void devolveIngredientes(List<ItemPedido> itens) {
        estoqueClient.post()
            .uri("/estoque/devolucao")
            .body(new MovimentacaoEstoqueRequest(toIngredientesRequest(calculaIngredientesNecessarios(itens))))
            .retrieve()
            .toBodilessEntity();
    }

    private Map<Long, Integer> calculaIngredientesNecessarios(List<ItemPedido> itens) {
        Map<Long, Integer> ingredientesNecessarios = new HashMap<>();
        for (ItemPedido item : itens) {
            calculaIngredientesNecessarios(item)
                .forEach((ingredienteId, quantidade) ->
                    ingredientesNecessarios.merge(ingredienteId, quantidade, Integer::sum));
        }
        return ingredientesNecessarios;
    }

    private List<IngredienteQuantidadeRequest> toIngredientesRequest(Map<Long, Integer> ingredientes) {
        return ingredientes.entrySet().stream()
            .map(entry -> new IngredienteQuantidadeRequest(entry.getKey(), entry.getValue()))
            .toList();
    }

    private Map<Long, Integer> calculaIngredientesNecessarios(ItemPedido itemPedido) {
        Map<Long, Integer> ingredientesNecessarios = new HashMap<>();
        for (Ingrediente ingrediente : itemPedido.getItem().getReceita().getIngredientes()) {
            ingredientesNecessarios.merge(ingrediente.getId(), itemPedido.getQuantidade(), Integer::sum);
        }
        return ingredientesNecessarios;
    }

    private record IngredienteQuantidadeRequest(long ingredienteId, int quantidade) {
    }

    private record ItemPedidoEstoqueRequest(long produtoId, List<IngredienteQuantidadeRequest> ingredientes) {
    }

    private record VerificacaoEstoqueRequest(List<ItemPedidoEstoqueRequest> itens) {
    }

    private record VerificacaoEstoqueResponse(List<Long> produtosSemEstoque) {
    }

    private record MovimentacaoEstoqueRequest(List<IngredienteQuantidadeRequest> ingredientes) {
    }
}
