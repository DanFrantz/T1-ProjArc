package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.ImpostoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.ImpostosResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ImpostosService;

@Component
public class ListarImpostosUC {
    private ImpostosService impostosService;

    public ListarImpostosUC(ImpostosService impostosService) {
        this.impostosService = impostosService;
    }

    public ImpostosResponse run() {
        return new ImpostosResponse(
            impostosService.listaImpostos().stream()
                .map(imposto -> new ImpostoResponse(
                    imposto.lei(),
                    imposto.descricao(),
                    imposto.corrente()))
                .toList());
    }
}
