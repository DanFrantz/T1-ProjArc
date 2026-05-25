package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;
 
public record AutenticarClienteResponse(String token, String email, String nome) {
}