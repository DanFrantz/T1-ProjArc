package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.DefinirPoliticaDescontoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PoliticaDescontoCorrenteResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.DescontosService;

@Component
public class DefinirPoliticaDescontoUC {
    private DescontosService descontosService;

    public DefinirPoliticaDescontoUC(DescontosService descontosService) {
        this.descontosService = descontosService;
    }

    public PoliticaDescontoCorrenteResponse run(DefinirPoliticaDescontoRequest request) {
        descontosService.definePoliticaCorrente(request.codigo());
        return new PoliticaDescontoCorrenteResponse(request.codigo(), "Politica de desconto corrente atualizada");
    }
}
