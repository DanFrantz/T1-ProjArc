package com.bcopstein.estoque.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "itens_estoque")
public class ItemEstoque {
    @Id
    private Long id;
    private int quantidade;

    @OneToOne
    @JoinColumn(name = "ingrediente_id", nullable = false)
    private Ingrediente ingrediente;

    protected ItemEstoque() {
    }

    public ItemEstoque(Long id, int quantidade, Ingrediente ingrediente) {
        this.id = id;
        this.quantidade = quantidade;
        this.ingrediente = ingrediente;
    }

    public Long getId() {
        return id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void baixa(int quantidade) {
        this.quantidade -= quantidade;
    }

    public void devolve(int quantidade) {
        this.quantidade += quantidade;
    }
}
