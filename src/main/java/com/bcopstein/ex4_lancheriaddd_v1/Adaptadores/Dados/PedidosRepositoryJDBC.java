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

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClientesRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
 
@Component
public class PedidosRepositoryJDBC implements PedidosRepository {
    private JdbcTemplate jdbcTemplate;
    private ProdutosRepository produtosRepository;
    private ClientesRepository clientesRepository;

    public PedidosRepositoryJDBC(JdbcTemplate jdbcTemplate, ProdutosRepository produtosRepository, ClientesRepository clientesRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.produtosRepository = produtosRepository;
        this.clientesRepository = clientesRepository;
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
    public List<ItemPedido> recuperaItensPorId(long idPedido) {
        String sql = "SELECT produto_id, quantidade FROM itens_pedido WHERE pedido_id = ?";
        return jdbcTemplate.query(
            sql,
            ps -> ps.setLong(1, idPedido),
            (rs, rowNum) -> {
                Produto produto = produtosRepository.recuperaProdutoPorid(rs.getLong("produto_id"));
                return new ItemPedido(produto, rs.getInt("quantidade"));
            });
    }

    @Override
    public void removePorId(long idPedido) {
        jdbcTemplate.update("DELETE FROM itens_pedido WHERE pedido_id = ?", idPedido);
        jdbcTemplate.update("DELETE FROM pedidos WHERE id = ?", idPedido);
    }

    @Override
    public int contaPedidosClienteDesde(String cpfCliente, LocalDateTime dataInicial) {
        String sql = "SELECT COUNT(*) FROM pedidos WHERE cliente_cpf = ? AND data_hora_pagamento >= ?";
        Integer quantidade = jdbcTemplate.queryForObject(sql, Integer.class, cpfCliente, Timestamp.valueOf(dataInicial));
        return quantidade == null ? 0 : quantidade;
    }
 
    @Override
    public void atualizaStatus(long idPedido, Pedido.Status status) {
        jdbcTemplate.update(
            "UPDATE pedidos SET status = ? WHERE id = ?",
            status.name(),
            idPedido);
    }
 
    @Override
    public List<Pedido> recuperaEntreguesEntre(LocalDateTime inicio, LocalDateTime fim) {
        String sql = """
            SELECT id, cliente_cpf, data_hora_pagamento, status, valor, impostos, desconto, valor_cobrado
            FROM pedidos
            WHERE status = 'ENTREGUE'
              AND data_hora_pagamento >= ?
              AND data_hora_pagamento <= ?
            """;
        return consultaPedidos(sql, Timestamp.valueOf(inicio), Timestamp.valueOf(fim));
    }
 
    @Override
    public List<Pedido> recuperaEntreguesClienteEntre(String cpfCliente, LocalDateTime inicio, LocalDateTime fim) {
        String sql = """
            SELECT id, cliente_cpf, data_hora_pagamento, status, valor, impostos, desconto, valor_cobrado
            FROM pedidos
            WHERE status = 'ENTREGUE'
              AND cliente_cpf = ?
              AND data_hora_pagamento >= ?
              AND data_hora_pagamento <= ?
            """;
        return consultaPedidos(sql, cpfCliente, Timestamp.valueOf(inicio), Timestamp.valueOf(fim));
    }
 
    private List<Pedido> consultaPedidos(String sql, Object... params) {
        return jdbcTemplate.query(
            sql,
            ps -> {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            },
            (rs, rowNum) -> {
                long id = rs.getLong("id");
                String cpf = rs.getString("cliente_cpf");
                Cliente cliente = clientesRepository.recuperaPorCpf(cpf);
                Timestamp ts = rs.getTimestamp("data_hora_pagamento");
                List<ItemPedido> itens = recuperaItensPorId(id);
                return new Pedido(
                    id,
                    cliente,
                    ts == null ? null : ts.toLocalDateTime(),
                    itens,
                    Pedido.Status.valueOf(rs.getString("status")),
                    rs.getDouble("valor"),
                    rs.getDouble("impostos"),
                    rs.getDouble("desconto"),
                    rs.getDouble("valor_cobrado"));
            });}

    @Override
    public void atualizaStatusEDataPagamento(long idPedido, Pedido.Status status, LocalDateTime dataHoraPagamento) {
        jdbcTemplate.update(
            "UPDATE pedidos SET status = ?, data_hora_pagamento = ? WHERE id = ?",
            status.name(),
            Timestamp.valueOf(dataHoraPagamento),
            idPedido);
    }
}
