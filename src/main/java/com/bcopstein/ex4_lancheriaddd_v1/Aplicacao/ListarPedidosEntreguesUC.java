package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;
 
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
 
import org.springframework.stereotype.Component;
 
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidosEntreguesResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidosEntreguesResponse.PedidoEntregueResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidosEntreguesResponse.PedidoEntregueResponse.ItemPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
 
@Component
public class ListarPedidosEntreguesUC {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private PedidosRepository pedidosRepository;
 
    public ListarPedidosEntreguesUC(PedidosRepository pedidosRepository) {
        this.pedidosRepository = pedidosRepository;
    }
 
    public PedidosEntreguesResponse run(LocalDateTime inicio, LocalDateTime fim) {
        List<Pedido> pedidos = pedidosRepository.recuperaEntreguesEntre(inicio, fim);
    return new PedidosEntreguesResponse(
    pedidos.stream()
        .map(p -> toResponse(p))
        .toList());}
 
    private PedidoEntregueResponse toResponse(Pedido pedido) {
        List<ItemPedidoResponse> itens = pedido.getItens().stream()
            .map(i -> new ItemPedidoResponse(i.getItem().getDescricao(), i.getQuantidade()))
            .toList();
        return new PedidoEntregueResponse(
            pedido.getId(),
            pedido.getCliente().getCpf(),
            pedido.getCliente().getNome(),
            pedido.getDataHoraPagamento() == null ? null : pedido.getDataHoraPagamento().format(FORMATTER),
            pedido.getValor(),
            pedido.getImpostos(),
            pedido.getDesconto(),
            pedido.getValorCobrado(),
            itens);
    }
}
