package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.AutenticacaoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.AutenticacaoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.AutenticacaoService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ResultadoAutenticacao;

@Component
public class AutenticarClienteUC {
    private AutenticacaoService autenticacaoService;

    public AutenticarClienteUC(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    public AutenticacaoResponse run(AutenticacaoRequest request) {
        ResultadoAutenticacao resultado = autenticacaoService.autentica(request.email(), request.senha());
        return new AutenticacaoResponse(resultado);
    }
}
