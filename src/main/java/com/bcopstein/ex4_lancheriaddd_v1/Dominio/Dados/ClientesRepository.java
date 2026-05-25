package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

public interface ClientesRepository {
    Cliente recuperaPorEmail(String email);
    Cliente salva(Cliente cliente);
    boolean existePorEmail(String email);
    Cliente recuperaPorCpf(String cpf);
}
