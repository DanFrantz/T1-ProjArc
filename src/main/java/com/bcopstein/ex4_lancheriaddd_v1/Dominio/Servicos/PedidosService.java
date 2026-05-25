package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClientesRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
 
@Service
public class PedidosService {
    private ClientesRepository clientesRepository;
    private ProdutosRepository produtosRepository;
    private EstoqueService estoqueService;
    private ImpostosService impostosService;
    private DescontosService descontosService;
    private PedidosRepository pedidosRepository;
    private IPagamentoService pagamentoService;
    private ICozinhaService cozinhaService;

    public PedidosService(
    ClientesRepository clientesRepository,
    ProdutosRepository produtosRepository,
    EstoqueService estoqueService,
    ImpostosService impostosService,
    DescontosService descontosService,
    PedidosRepository pedidosRepository,
    IPagamentoService pagamentoService,
    ICozinhaService cozinhaService){
    this.clientesRepository = clientesRepository;
    this.produtosRepository = produtosRepository;
    this.estoqueService = estoqueService;
    this.impostosService = impostosService;
    this.descontosService = descontosService;
    this.pedidosRepository = pedidosRepository;
    this.pagamentoService = pagamentoService;
    this.cozinhaService = cozinhaService;
    }

    @Transactional
    public ResultadoPedidoAprovacao submeteParaAprovacao(String emailCliente,
    String enderecoEntrega,
    List<ItemPedidoSolicitado> itensSolicitados) {
        Cliente cliente = clientesRepository.recuperaPorEmail(emailCliente);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente nao encontrado");
        }

        List<ItemPedido> itens = itensSolicitados.stream()
            .map(item -> criaItemPedido(item.produtoId(), item.quantidade()))
            .toList();

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
 
    @Transactional
    public void pagaPedido(long idPedido) {
        Pedido.Status status = consultaStatus(idPedido);
        if (status != Pedido.Status.APROVADO) {
            throw new IllegalStateException("Pedido nao pode ser pago no status: " + status.name());
        }
 
        boolean pagamentoAprovado = pagamentoService.processaPagamento(idPedido, 0);
        if (!pagamentoAprovado) {
            throw new IllegalStateException("Pagamento recusado para o pedido: " + idPedido);
        }

        pedidosRepository.atualizaStatus(idPedido, Pedido.Status.PAGO);
        cozinhaService.chegadaDePedido(idPedido);
    }

    public Pedido.Status consultaStatus(long idPedido) {
        Pedido.Status status = pedidosRepository.recuperaStatusPorId(idPedido);
        if (status == null) {
            throw new IllegalArgumentException("Pedido nao encontrado: " + idPedido);
        }
        return status;
    }

    @Transactional
    public void cancelaPedido(long idPedido) {
        Pedido.Status status = consultaStatus(idPedido);
        if (status != Pedido.Status.APROVADO) {
            throw new IllegalStateException("Pedido nao pode ser cancelado no status: " + status.name());
        }

        List<ItemPedido> itens = pedidosRepository.recuperaItensPorId(idPedido);
        estoqueService.devolveIngredientes(itens);
        pedidosRepository.removePorId(idPedido);
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