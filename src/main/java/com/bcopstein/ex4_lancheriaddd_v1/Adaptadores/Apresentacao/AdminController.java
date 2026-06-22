package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.DefinirCardapioCorrenteUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.DefinirPoliticaDescontoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ListarPoliticasDescontoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.DefinirCardapioCorrenteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.DefinirPoliticaDescontoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CardapioCorrenteResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PoliticaDescontoCorrenteResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PoliticasDescontoResponse;

@RestController
@RequestMapping("/admin")
public record AdminController(DefinirCardapioCorrenteUC definirCardapioCorrenteUC,
                              ListarPoliticasDescontoUC listarPoliticasDescontoUC,
                              DefinirPoliticaDescontoUC definirPoliticaDescontoUC) {

    @PutMapping("/cardapio-corrente")
    @CrossOrigin("*")
    public CardapioCorrenteResponse defineCardapioCorrente(@RequestBody DefinirCardapioCorrenteRequest request) {
        return definirCardapioCorrenteUC.run(request);
    }

    @GetMapping("/descontos")
    @CrossOrigin("*")
    public PoliticasDescontoResponse listaPoliticasDesconto() {
        return listarPoliticasDescontoUC.run();
    }

    @PutMapping("/desconto-corrente")
    @CrossOrigin("*")
    public PoliticaDescontoCorrenteResponse definePoliticaDescontoCorrente(
            @RequestBody DefinirPoliticaDescontoRequest request) {
        return definirPoliticaDescontoUC.run(request);
    }
}
