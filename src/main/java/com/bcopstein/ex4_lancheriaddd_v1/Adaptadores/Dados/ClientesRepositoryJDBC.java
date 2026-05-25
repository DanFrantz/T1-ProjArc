package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClientesRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@Component
public class ClientesRepositoryJDBC implements ClientesRepository {
    private JdbcTemplate jdbcTemplate;

    public ClientesRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Cliente recuperaPorEmail(String email) {
        String sql = "SELECT cpf, nome, celular, endereco, email, senha FROM clientes WHERE email = ?";
        List<Cliente> clientes = jdbcTemplate.query(
            sql,
            ps -> ps.setString(1, email),
            (rs, rowNum) -> new Cliente(
                rs.getString("cpf"),
                rs.getString("nome"),
                rs.getString("celular"),
                rs.getString("endereco"),
                rs.getString("email"),
                rs.getString("senha"))
        );
        return clientes.isEmpty() ? null : clientes.getFirst();
    }

    @Override
    public Cliente salva(Cliente cliente){
        String sql = """
            INSERT INTO clientes (cpf, nome, celular, endereco, email, senha)
            VALUES (?,?,?,?,?,?)
            """;
        jdbcTemplate.update(sql,
            cliente.getCpf(),
            cliente.getNome(),
            cliente.getCelular(),
            cliente.getEndereco(),
            cliente.getEmail(),
            cliente.getSenha());
        return cliente;
    }
    @Override
    public boolean existePorEmail(String email) {
        String sql = "SELECT COUNT(*) FROM clientes WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }
}
