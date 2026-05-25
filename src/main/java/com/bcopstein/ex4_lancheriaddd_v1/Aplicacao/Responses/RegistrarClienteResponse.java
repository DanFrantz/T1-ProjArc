package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

public record RegistrarClienteResponse(
    String cpf,
    String nome,
    String email,
    String mensagem) {
}