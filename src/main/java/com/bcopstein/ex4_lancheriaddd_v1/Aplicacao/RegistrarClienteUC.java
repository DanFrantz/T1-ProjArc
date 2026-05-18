package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.RegistrarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.ClienteResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.CadastroClientesService;

@Component
public class RegistrarClienteUC {
    private CadastroClientesService cadastroClientesService;

    public RegistrarClienteUC(CadastroClientesService cadastroClientesService) {
        this.cadastroClientesService = cadastroClientesService;
    }

    public ClienteResponse run(RegistrarClienteRequest request) {
        Cliente cliente = cadastroClientesService.registra(
            request.nome(),
            request.cpf(),
            request.celular(),
            request.endereco(),
            request.email(),
            request.senha());
        return new ClienteResponse(cliente);
    }
}
