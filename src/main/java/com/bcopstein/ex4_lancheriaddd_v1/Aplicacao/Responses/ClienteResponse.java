package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

public record ClienteResponse(String nome,
                              String cpf,
                              String celular,
                              String endereco,
                              String email) {
    public ClienteResponse(Cliente cliente) {
        this(cliente.getNome(),
             cliente.getCpf(),
             cliente.getCelular(),
             cliente.getEndereco(),
             cliente.getEmail());
    }
}
