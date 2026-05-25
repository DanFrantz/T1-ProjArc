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
public class EntregaService implements IEntregaService {
    private PedidosRepository pedidosRepository;
    private Queue<Long> filaEntrega;
    private ScheduledExecutorService scheduler;
 
    public EntregaService(PedidosRepository pedidosRepository) {
        this.pedidosRepository = pedidosRepository;
        this.filaEntrega = new LinkedBlockingQueue<>();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }
 
    @Override
    public synchronized void chegadaDePedido(long idPedido) {
        filaEntrega.add(idPedido);
        System.out.println("Pedido " + idPedido + " na fila de entrega");
        scheduler.schedule(() -> iniciaEntrega(idPedido), 5, TimeUnit.SECONDS);
    }
 
    private synchronized void iniciaEntrega(long idPedido) {
        pedidosRepository.atualizaStatus(idPedido, Pedido.Status.TRANSPORTE);
        System.out.println("Pedido " + idPedido + " em transporte");
        scheduler.schedule(() -> finalizaEntrega(idPedido), 10, TimeUnit.SECONDS);
    }
 
    private synchronized void finalizaEntrega(long idPedido) {
        filaEntrega.remove(idPedido);
        pedidosRepository.atualizaStatus(idPedido, Pedido.Status.ENTREGUE);
        System.out.println("Pedido " + idPedido + " entregue");
    }
}

