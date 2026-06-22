package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;
 
import java.time.LocalDateTime;
import java.util.List;
 
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
 
public interface PedidosRepository {
    Pedido salva(Pedido pedido, String enderecoEntrega);
    Pedido.Status recuperaStatusPorId(long idPedido);
    List<ItemPedido> recuperaItensPorId(long idPedido);
    void removePorId(long idPedido);
    int contaPedidosClienteDesde(String cpfCliente, LocalDateTime dataInicial);
    void atualizaStatus(long idPedido, Pedido.Status status);
    void registraStatus(long idPedido, Pedido.Status status, LocalDateTime dataHora);
    List<Pedido> recuperaEntreguesEntre(LocalDateTime inicio, LocalDateTime fim);
    List<Pedido> recuperaEntreguesClienteEntre(String cpfCliente, LocalDateTime inicio, LocalDateTime fim);
    void atualizaStatusEDataPagamento(long idPedido, Pedido.Status status, LocalDateTime dataHoraPagamento);
}
