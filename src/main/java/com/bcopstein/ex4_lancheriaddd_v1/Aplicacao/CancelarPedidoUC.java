package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CancelamentoPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidosService;

@Component
public class CancelarPedidoUC {
    private PedidosService pedidosService;

    public CancelarPedidoUC(PedidosService pedidosService) {
        this.pedidosService = pedidosService;
    }

    public CancelamentoPedidoResponse run(long idPedido) {
        pedidosService.cancelaPedido(idPedido);
        return new CancelamentoPedidoResponse(idPedido, "Pedido cancelado com sucesso");
    }
}
