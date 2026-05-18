package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.StatusPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidosService;

@Component
public class SolicitarStatusPedidoUC {
    private PedidosService pedidosService;

    public SolicitarStatusPedidoUC(PedidosService pedidosService) {
        this.pedidosService = pedidosService;
    }

    public StatusPedidoResponse run(long idPedido) {
        Pedido.Status status = pedidosService.consultaStatus(idPedido);
        return new StatusPedidoResponse(idPedido, status);
    }
}
