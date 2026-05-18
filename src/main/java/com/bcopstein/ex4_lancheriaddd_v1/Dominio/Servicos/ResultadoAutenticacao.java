package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

public record ResultadoAutenticacao(boolean autenticado,
                                    String email,
                                    String nome,
                                    String token,
                                    String mensagem) {
}
