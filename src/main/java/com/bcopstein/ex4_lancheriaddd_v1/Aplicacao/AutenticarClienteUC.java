package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;
 
import org.springframework.stereotype.Component;
 
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.AutenticarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.AutenticarClienteResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClientesRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.JwtService;
 
@Component
public class AutenticarClienteUC {
    private ClientesRepository clientesRepository;
    private JwtService jwtService;
 
    public AutenticarClienteUC(ClientesRepository clientesRepository, JwtService jwtService) {
        this.clientesRepository = clientesRepository;
        this.jwtService = jwtService;
    }
 
    public AutenticarClienteResponse run(AutenticarClienteRequest request) {
        Cliente cliente = clientesRepository.recuperaPorEmail(request.email());
 
        if (cliente == null || !cliente.getSenha().equals(request.senha())) {
            throw new IllegalArgumentException("Email ou senha invalidos");
        }
 
        String token = jwtService.geraToken(cliente.getEmail());
        return new AutenticarClienteResponse(token, cliente.getEmail(), cliente.getNome());
    }
}