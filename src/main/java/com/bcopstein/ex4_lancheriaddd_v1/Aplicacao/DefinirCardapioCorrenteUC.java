package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.DefinirCardapioCorrenteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CardapioCorrenteResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.CardapioService;

@Component
public class DefinirCardapioCorrenteUC {
    private CardapioService cardapioService;

    public DefinirCardapioCorrenteUC(CardapioService cardapioService) {
        this.cardapioService = cardapioService;
    }

    public CardapioCorrenteResponse run(DefinirCardapioCorrenteRequest request) {
        cardapioService.defineCardapioCorrente(request.idCardapio());
        return new CardapioCorrenteResponse(request.idCardapio(), "Cardapio corrente atualizado");
    }
}
