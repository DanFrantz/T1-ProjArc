package com.bcopstein.estoque.dados;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bcopstein.estoque.dominio.ItemEstoque;

public interface ItemEstoqueRepository extends JpaRepository<ItemEstoque, Long> {
    Optional<ItemEstoque> findByIngredienteId(Long ingredienteId);
}
