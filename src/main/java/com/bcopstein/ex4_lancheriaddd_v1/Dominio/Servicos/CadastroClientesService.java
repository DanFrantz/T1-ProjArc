package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClientesRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@Service
public class CadastroClientesService {
    private ClientesRepository clientesRepository;

    public CadastroClientesService(ClientesRepository clientesRepository) {
        this.clientesRepository = clientesRepository;
    }

    public Cliente registra(String nome,
                            String cpf,
                            String celular,
                            String endereco,
                            String email,
                            String senha) {
        validaObrigatorio(nome, "Nome");
        validaObrigatorio(cpf, "CPF");
        validaObrigatorio(celular, "Celular");
        validaObrigatorio(endereco, "Endereco");
        validaObrigatorio(email, "Email");
        validaObrigatorio(senha, "Senha");

        if (clientesRepository.recuperaPorCpf(cpf) != null) {
            throw new IllegalArgumentException("Ja existe cliente cadastrado com este CPF");
        }
        if (clientesRepository.recuperaPorEmail(email) != null) {
            throw new IllegalArgumentException("Ja existe cliente cadastrado com este email");
        }

        Cliente cliente = new Cliente(cpf, nome, celular, endereco, email, senha);
        return clientesRepository.salva(cliente);
    }

    private void validaObrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " eh obrigatorio");
        }
    }
}
