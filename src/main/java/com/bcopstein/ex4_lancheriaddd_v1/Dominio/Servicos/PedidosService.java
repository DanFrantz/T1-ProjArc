package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

        Map<Long, Produto> produtosIndisponiveis = new LinkedHashMap<>();
        itens.stream()
            .map(ItemPedido::getItem)
            .filter(produto -> !produto.isDisponivel())
            .forEach(produto -> produtosIndisponiveis.put(produto.getId(), produto));

        estoqueService.produtosSemEstoque(itens).forEach(produto -> {
            produtosRepository.marcaIndisponivel(produto.getId());
            produtosIndisponiveis.put(produto.getId(), produto);
        });

        if (!produtosIndisponiveis.isEmpty()) {
            Pedido pedido = new Pedido(0, cliente, null, itens, Pedido.Status.NOVO, 0, 0, 0, 0);
            return new ResultadoPedidoAprovacao(pedido, List.copyOf(produtosIndisponiveis.values()));
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
            Pedido.Status.NOVO,
            valorItens,
            impostos,
            desconto,
            valorCobrado);
        estoqueService.baixaIngredientes(itens);
        Pedido pedidoSalvo = pedidosRepository.salva(pedido, enderecoEntrega);
        pedidosRepository.atualizaStatus(pedidoSalvo.getId(), Pedido.Status.APROVADO);
        Pedido pedidoAprovado = new Pedido(
            pedidoSalvo.getId(),
            pedidoSalvo.getCliente(),
            pedidoSalvo.getDataHoraPagamento(),
            pedidoSalvo.getItens(),
            Pedido.Status.APROVADO,
            pedidoSalvo.getValor(),
            pedidoSalvo.getImpostos(),
            pedidoSalvo.getDesconto(),
            pedidoSalvo.getValorCobrado());
        return new ResultadoPedidoAprovacao(pedidoAprovado, List.of());
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

        pedidosRepository.atualizaStatusEDataPagamento(idPedido, Pedido.Status.PAGO, LocalDateTime.now());
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
