package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClientesRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@Service
public class AutenticacaoService {
    private ClientesRepository clientesRepository;

    public AutenticacaoService(ClientesRepository clientesRepository) {
        this.clientesRepository = clientesRepository;
    }

    public ResultadoAutenticacao autentica(String email, String senha) {
        Cliente cliente = clientesRepository.recuperaPorEmail(email);
        if (cliente == null || !cliente.getSenha().equals(senha)) {
            return new ResultadoAutenticacao(false, email, null, null, "Usuario ou senha invalidos");
        }

        return new ResultadoAutenticacao(
            true,
            cliente.getEmail(),
            cliente.getNome(),
            geraTokenSimples(cliente),
            "Cliente autenticado com sucesso");
    }

    private String geraTokenSimples(Cliente cliente) {
        return "AUTH-" + cliente.getCpf();
    }
}
