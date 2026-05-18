package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ItemPedidoSolicitado;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidosService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ResultadoPedidoAprovacao;

@Component
public class SubmeterPedidoParaAprovacaoUC {
    private PedidosService pedidosService;

    public SubmeterPedidoParaAprovacaoUC(PedidosService pedidosService) {
        this.pedidosService = pedidosService;
    }

    public PedidoResponse run(SubmeterPedidoRequest request) {
        List<ItemPedidoSolicitado> itens = request.itens().stream()
            .map(item -> new ItemPedidoSolicitado(item.produtoId(), item.quantidade()))
            .toList();

        ResultadoPedidoAprovacao resultado = pedidosService.submeteParaAprovacao(
            request.emailCliente(),
            request.enderecoEntrega(),
            itens);
        return new PedidoResponse(resultado);
    }
}
