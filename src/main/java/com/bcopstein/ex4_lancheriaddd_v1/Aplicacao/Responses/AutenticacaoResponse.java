package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ResultadoAutenticacao;

public record AutenticacaoResponse(boolean autenticado,
                                   String email,
                                   String nome,
                                   String token,
                                   String mensagem) {
    public AutenticacaoResponse(ResultadoAutenticacao resultado) {
        this(resultado.autenticado(),
             resultado.email(),
             resultado.nome(),
             resultado.token(),
             resultado.mensagem());
    }
}
