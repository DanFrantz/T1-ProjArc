package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ValidarCredenciaisClienteUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.AutenticarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CredenciaisClienteResponse;

@RestController
@RequestMapping("/internal/auth")
public record InternalAuthController(ValidarCredenciaisClienteUC validarCredenciaisClienteUC) {

    @PostMapping("/validate")
    public CredenciaisClienteResponse validaCredenciais(@RequestBody AutenticarClienteRequest request) {
        return validarCredenciaisClienteUC.run(request);
    }
}
