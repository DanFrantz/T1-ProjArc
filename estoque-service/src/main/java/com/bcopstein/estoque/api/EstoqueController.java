package com.bcopstein.estoque.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.estoque.aplicacao.EstoqueService;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {
    private EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @PostMapping("/verificacao")
    public VerificacaoEstoqueResponse verifica(@RequestBody VerificacaoEstoqueRequest request) {
        return estoqueService.verifica(request);
    }

    @PostMapping("/baixa")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void baixa(@RequestBody MovimentacaoEstoqueRequest request) {
        estoqueService.baixa(request);
    }

    @PostMapping("/devolucao")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void devolve(@RequestBody MovimentacaoEstoqueRequest request) {
        estoqueService.devolve(request);
    }
}
