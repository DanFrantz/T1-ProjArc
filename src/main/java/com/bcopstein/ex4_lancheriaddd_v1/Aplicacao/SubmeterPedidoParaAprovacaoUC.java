package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClientesRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidosService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ResultadoPedidoAprovacao;

@Component
public class SubmeterPedidoParaAprovacaoUC {
    private ClientesRepository clientesRepository;
    private ProdutosRepository produtosRepository;
    private PedidosService pedidosService;

    public SubmeterPedidoParaAprovacaoUC(ClientesRepository clientesRepository,
                                         ProdutosRepository produtosRepository,
                                         PedidosService pedidosService) {
        this.clientesRepository = clientesRepository;
        this.produtosRepository = produtosRepository;
        this.pedidosService = pedidosService;
    }

    public PedidoResponse run(SubmeterPedidoRequest request) {
        Cliente cliente = clientesRepository.recuperaPorEmail(request.emailCliente());
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente nao encontrado");
        }

        List<ItemPedido> itens = request.itens().stream()
            .map(item -> criaItemPedido(item.produtoId(), item.quantidade()))
            .toList();

        ResultadoPedidoAprovacao resultado = pedidosService.aprovaPedido(cliente, request.enderecoEntrega(), itens);
        return new PedidoResponse(resultado);
    }

    private ItemPedido criaItemPedido(long produtoId, int quantidade) {
        Produto produto = produtosRepository.recuperaProdutoPorid(produtoId);
        if (produto == null) {
            throw new IllegalArgumentException("Produto nao encontrado: " + produtoId);
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade invalida: " + quantidade);
        }
        return new ItemPedido(produto, quantidade);
    }
}
