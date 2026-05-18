package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.RegistrarClienteUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.RegistrarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.ClienteResponse;

@RestController
@RequestMapping("/clientes")
public record ClientesController(RegistrarClienteUC registrarClienteUC) {
    @PostMapping
    @CrossOrigin("*")
    public ClienteResponse registra(@RequestBody RegistrarClienteRequest request) {
        return registrarClienteUC.run(request);
    }
}
