package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.Descontos;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PoliticaDesconto;

@Component
public class PoliticaFidelidade7 implements PoliticaDesconto {
    private PedidosRepository pedidosRepository;

    public PoliticaFidelidade7(PedidosRepository pedidosRepository) {
        this.pedidosRepository = pedidosRepository;
    }

    @Override
    public String codigo() {
        return "FIDELIDADE_7";
    }

    @Override
    public String descricao() {
        return "7% para clientes com mais de 3 pedidos nos ultimos 20 dias";
    }

    @Override
    public double calcula(Cliente cliente, double valorItens) {
        LocalDateTime vinteDiasAtras = LocalDateTime.now().minusDays(20);
        int pedidosRecentes = pedidosRepository.contaPedidosClienteDesde(cliente.getCpf(), vinteDiasAtras);
        if (pedidosRecentes > 3) {
            return valorItens * 0.07;
        }
        return 0;
    }
}
