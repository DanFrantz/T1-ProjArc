package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Component
public class PedidosRepositoryJDBC implements PedidosRepository {
    private JdbcTemplate jdbcTemplate;

    public PedidosRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Pedido salva(Pedido pedido, String enderecoEntrega) {
        String sql = """
            INSERT INTO pedidos
                (cliente_cpf, endereco_entrega, data_hora_pagamento, status, valor, impostos, desconto, valor_cobrado)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pedido.getCliente().getCpf());
            ps.setString(2, enderecoEntrega);
            ps.setTimestamp(3, pedido.getDataHoraPagamento() == null ? null : Timestamp.valueOf(pedido.getDataHoraPagamento()));
            ps.setString(4, pedido.getStatus().name());
            ps.setDouble(5, pedido.getValor());
            ps.setDouble(6, pedido.getImpostos());
            ps.setDouble(7, pedido.getDesconto());
            ps.setDouble(8, pedido.getValorCobrado());
            return ps;
        }, keyHolder);

        long idPedido = keyHolder.getKey().longValue();
        for (ItemPedido item : pedido.getItens()) {
            jdbcTemplate.update(
                "INSERT INTO itens_pedido (pedido_id, produto_id, quantidade) VALUES (?, ?, ?)",
                idPedido,
                item.getItem().getId(),
                item.getQuantidade());
        }

        return new Pedido(
            idPedido,
            pedido.getCliente(),
            pedido.getDataHoraPagamento(),
            pedido.getItens(),
            pedido.getStatus(),
            pedido.getValor(),
            pedido.getImpostos(),
            pedido.getDesconto(),
            pedido.getValorCobrado());
    }

    @Override
    public Pedido.Status recuperaStatusPorId(long idPedido) {
        String sql = "SELECT status FROM pedidos WHERE id = ?";
        List<Pedido.Status> status = jdbcTemplate.query(
            sql,
            ps -> ps.setLong(1, idPedido),
            (rs, rowNum) -> Pedido.Status.valueOf(rs.getString("status")));
        return status.isEmpty() ? null : status.getFirst();
    }

    @Override
    public int contaPedidosClienteDesde(String cpfCliente, LocalDateTime dataInicial) {
        String sql = "SELECT COUNT(*) FROM pedidos WHERE cliente_cpf = ? AND data_hora_pagamento >= ?";
        Integer quantidade = jdbcTemplate.queryForObject(sql, Integer.class, cpfCliente, Timestamp.valueOf(dataInicial));
        return quantidade == null ? 0 : quantidade;
    }
}
