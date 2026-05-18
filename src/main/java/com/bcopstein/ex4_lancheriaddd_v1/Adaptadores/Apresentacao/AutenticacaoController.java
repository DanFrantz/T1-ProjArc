package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.AutenticarClienteUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.AutenticacaoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.AutenticacaoResponse;

@RestController
@RequestMapping("/auth")
public record AutenticacaoController(AutenticarClienteUC autenticarClienteUC) {
    @PostMapping("/login")
    @CrossOrigin("*")
    public AutenticacaoResponse autentica(@RequestBody AutenticacaoRequest request) {
        return autenticarClienteUC.run(request);
    }
}
