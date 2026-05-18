package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CardapioResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

public class CardapioPresenter {
    public class ItemCardapioPresenter{
        private Long id;
        private String descricao;
        private int preco;
        private boolean indicacao;
        
        public ItemCardapioPresenter(Long id, String descricao, int preco, boolean indicacao) {
            this.id = id;
            this.descricao = descricao;
            this.preco = preco;
            this.indicacao = indicacao;
        }

        public Long getId() {
            return id;
        }

        public String getDescricao() {
            return descricao;
        }

        public int getPreco() {
            return preco;
        }

        public boolean isIndicacao() {
            return indicacao;
        }
    }

    private String titulo;
    private List<ItemCardapioPresenter> itens;

    public CardapioPresenter(String titulo){
        this.titulo = titulo;
        itens = new LinkedList<>();
    }

    public String getTitulo(){
        return titulo;
    }

    public void insereItem(long id,String titulo,int preco,boolean sugestao){
        itens.add(new ItemCardapioPresenter(id, titulo, preco, sugestao));
    }
    
    public List<ItemCardapioPresenter> getItens() {
        return itens;
    }

    public void add(ItemCardapioPresenter item){
        itens.add(item);
    }

    public static CardapioPresenter from(CardapioResponse cardapioResponse) {
        Set<Long> conjIdSugestoes = new HashSet<>(cardapioResponse.getSugestoesDoChef().stream()
            .map(produto -> produto.getId())
            .toList());
        CardapioPresenter cardapioPresenter = new CardapioPresenter(cardapioResponse.getCardapio().getCabecalhoCardapio().titulo());
        for (Produto produto : cardapioResponse.getCardapio().getProdutos()) {
            boolean sugestao = conjIdSugestoes.contains(produto.getId());
            cardapioPresenter.insereItem(produto.getId(), produto.getDescricao(), produto.getPreco(), sugestao);
        }
        return cardapioPresenter;
    }
}
