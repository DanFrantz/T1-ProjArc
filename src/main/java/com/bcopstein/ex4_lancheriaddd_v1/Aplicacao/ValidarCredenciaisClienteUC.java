package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.AutenticarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CredenciaisClienteResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClientesRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@Component
public class ValidarCredenciaisClienteUC {
    private ClientesRepository clientesRepository;

    public ValidarCredenciaisClienteUC(ClientesRepository clientesRepository) {
        this.clientesRepository = clientesRepository;
    }

    public CredenciaisClienteResponse run(AutenticarClienteRequest request) {
        Cliente cliente = clientesRepository.recuperaPorEmail(request.email());
        if (cliente == null || !cliente.getSenha().equals(request.senha())) {
            throw new IllegalArgumentException("Email ou senha invalidos");
        }
        return new CredenciaisClienteResponse(cliente.getEmail(), cliente.getNome());
    }
}
