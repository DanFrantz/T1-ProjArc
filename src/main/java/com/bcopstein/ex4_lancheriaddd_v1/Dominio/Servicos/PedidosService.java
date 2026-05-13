package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Service
public class PedidosService {
    private EstoqueService estoqueService;
    private ImpostosService impostosService;
    private DescontosService descontosService;
    private PedidosRepository pedidosRepository;

    public PedidosService(EstoqueService estoqueService,
                          ImpostosService impostosService,
                          DescontosService descontosService,
                          PedidosRepository pedidosRepository) {
        this.estoqueService = estoqueService;
        this.impostosService = impostosService;
        this.descontosService = descontosService;
        this.pedidosRepository = pedidosRepository;
    }

    @Transactional
    public ResultadoPedidoAprovacao aprovaPedido(Cliente cliente, String enderecoEntrega, List<ItemPedido> itens) {
        var produtosSemEstoque = estoqueService.produtosSemEstoque(itens);
        if (!produtosSemEstoque.isEmpty()) {
            Pedido pedido = new Pedido(0, cliente, null, itens, Pedido.Status.NOVO, 0, 0, 0, 0);
            return new ResultadoPedidoAprovacao(pedido, produtosSemEstoque);
        }

        double valorItens = itens.stream()
            .mapToDouble(item -> item.getItem().getPreco() * item.getQuantidade())
            .sum();
        double desconto = descontosService.calcula(cliente, valorItens);
        double impostos = impostosService.calcula(valorItens);
        double valorCobrado = valorItens - desconto + impostos;

        Pedido pedido = new Pedido(
            0,
            cliente,
            null,
            itens,
            Pedido.Status.APROVADO,
            valorItens,
            impostos,
            desconto,
            valorCobrado);
        estoqueService.baixaIngredientes(itens);
        Pedido pedidoSalvo = pedidosRepository.salva(pedido, enderecoEntrega);
        return new ResultadoPedidoAprovacao(pedidoSalvo, List.of());
    }
}
