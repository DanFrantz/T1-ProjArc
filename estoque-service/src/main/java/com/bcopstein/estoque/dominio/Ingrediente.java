package com.bcopstein.estoque.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ingredientes")
public class Ingrediente {
    @Id
    private Long id;
    private String descricao;

    protected Ingrediente() {
    }

    public Ingrediente(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }
}
