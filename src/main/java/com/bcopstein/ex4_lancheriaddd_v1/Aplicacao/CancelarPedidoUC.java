package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CancelamentoPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.EstoqueService;

@Component
public class CancelarPedidoUC {
    private PedidosRepository pedidosRepository;
    private EstoqueService estoqueService;

    public CancelarPedidoUC(PedidosRepository pedidosRepository, EstoqueService estoqueService) {
        this.pedidosRepository = pedidosRepository;
        this.estoqueService = estoqueService;
    }

    @Transactional
    public CancelamentoPedidoResponse run(long idPedido) {
        Pedido.Status status = pedidosRepository.recuperaStatusPorId(idPedido);
        if (status == null) {
            throw new IllegalArgumentException("Pedido nao encontrado: " + idPedido);
        }
        if (status != Pedido.Status.APROVADO) {
            throw new IllegalStateException("Pedido nao pode ser cancelado no status: " + status.name());
        }

        List<ItemPedido> itens = pedidosRepository.recuperaItensPorId(idPedido);
        estoqueService.devolveIngredientes(itens);
        pedidosRepository.removePorId(idPedido);
        return new CancelamentoPedidoResponse(idPedido, "Pedido cancelado com sucesso");
    }
}
