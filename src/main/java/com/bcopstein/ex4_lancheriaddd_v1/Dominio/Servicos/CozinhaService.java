package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;
 
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Service
public class CozinhaService implements ICozinhaService {
    private PedidosRepository pedidosRepository;
    private IEntregaService entregaService;
    private Queue<Long> filaEntrada;
    private Long emPreparacao;
    private ScheduledExecutorService scheduler;

    public CozinhaService(PedidosRepository pedidosRepository, IEntregaService entregaService) {
        this.pedidosRepository = pedidosRepository;
        this.entregaService = entregaService;
        this.filaEntrada = new LinkedBlockingQueue<>();
        this.emPreparacao = null;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public synchronized void chegadaDePedido(long idPedido) {
        pedidosRepository.atualizaStatus(idPedido, Pedido.Status.AGUARDANDO);
        filaEntrada.add(idPedido);
        System.out.println("Pedido " + idPedido + " aguardando na cozinha");
        if (emPreparacao == null) {
            colocaEmPreparacao(filaEntrada.poll());
        }
    }
 
    private synchronized void colocaEmPreparacao(long idPedido) {
        emPreparacao = idPedido;
        pedidosRepository.atualizaStatus(idPedido, Pedido.Status.PREPARACAO);
        System.out.println("Pedido " + idPedido + " em preparacao");
        scheduler.schedule(() -> pedidoPronto(), 5, TimeUnit.SECONDS);
    }

    @Override
    public synchronized void pedidoPronto() {
        long idPedido = emPreparacao;
        pedidosRepository.atualizaStatus(idPedido, Pedido.Status.PRONTO);
        System.out.println("Pedido " + idPedido + " pronto");
        emPreparacao = null;
       entregaService.chegadaDePedido(idPedido);
         if (!filaEntrada.isEmpty()) {
            long proximo = filaEntrada.poll();
            scheduler.schedule(() -> colocaEmPreparacao(proximo), 1, TimeUnit.SECONDS);
        }
    }
}
