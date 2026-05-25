package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.stereotype.Component;
 
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.RegistrarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.RegistrarClienteResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClientesRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
 
@Component
public class RegistrarClienteUC {
    private ClientesRepository clientesRepository;
 
    public RegistrarClienteUC(ClientesRepository clientesRepository) {
        this.clientesRepository = clientesRepository;
    }
 
    public RegistrarClienteResponse run(RegistrarClienteRequest request) {
        if (clientesRepository.existePorEmail(request.email())) {
            throw new IllegalArgumentException("Email ja cadastrado: " + request.email());
        }
 
        Cliente cliente = new Cliente(
            request.cpf(),
            request.nome(),
            request.celular(),
            request.endereco(),
            request.email(),
            request.senha());
 
        clientesRepository.salva(cliente);
 
        return new RegistrarClienteResponse(
            cliente.getCpf(),
            cliente.getNome(),
            cliente.getEmail(),
            "Cliente registrado com sucesso");
    }
}
