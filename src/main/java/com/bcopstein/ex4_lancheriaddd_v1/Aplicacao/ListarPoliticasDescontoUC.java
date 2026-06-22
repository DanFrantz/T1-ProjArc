package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PoliticaDescontoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PoliticasDescontoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.DescontosService;

@Component
public class ListarPoliticasDescontoUC {
    private DescontosService descontosService;

    public ListarPoliticasDescontoUC(DescontosService descontosService) {
        this.descontosService = descontosService;
    }

    public PoliticasDescontoResponse run() {
        return new PoliticasDescontoResponse(
            descontosService.listaPoliticas().stream()
                .map(politica -> new PoliticaDescontoResponse(
                    politica.codigo(),
                    politica.descricao(),
                    politica.corrente()))
                .toList());
    }
}
