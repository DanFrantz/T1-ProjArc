package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests;
 
public record RegistrarClienteRequest(
    String cpf,
    String nome,
    String celular,
    String endereco,
    String email,
    String senha) {
}