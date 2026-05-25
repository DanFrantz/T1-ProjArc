package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;
 
import java.time.LocalDateTime;
 
import org.springframework.stereotype.Component;
 
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PagarPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidosService;
 
@Component
public class PagarPedidoUC {
    private PedidosService pedidosService;
 
    public PagarPedidoUC(PedidosService pedidosService) {
        this.pedidosService = pedidosService;
    }
 
    public PagarPedidoResponse run(long idPedido) {
        pedidosService.pagaPedido(idPedido);
        return new PagarPedidoResponse(idPedido, "Pedido pago e encaminhado para a cozinha");
    }
}
