package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@Service
public class DescontosService {
    private PedidosRepository pedidosRepository;

    public DescontosService(PedidosRepository pedidosRepository) {
        this.pedidosRepository = pedidosRepository;
    }

    public double calcula(Cliente cliente, double valorItens) {
        LocalDateTime vinteDiasAtras = LocalDateTime.now().minusDays(20);
        int pedidosRecentes = pedidosRepository.contaPedidosClienteDesde(cliente.getCpf(), vinteDiasAtras);
        if (pedidosRecentes > 3) {
            return valorItens * 0.07;
        }
        return 0;
    }
}
