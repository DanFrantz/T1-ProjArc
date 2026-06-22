package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.DescontoCorrenteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@Service
public class DescontosService {
    private PedidosRepository pedidosRepository;
    private DescontoCorrenteRepository descontoCorrenteRepository;
    private Map<String, PoliticaDesconto> politicas;

    public DescontosService(PedidosRepository pedidosRepository,
                            DescontoCorrenteRepository descontoCorrenteRepository) {
        this.pedidosRepository = pedidosRepository;
        this.descontoCorrenteRepository = descontoCorrenteRepository;
        this.politicas = new LinkedHashMap<>();
        registraPoliticas();
    }

    public double calcula(Cliente cliente, double valorItens) {
        return politicaCorrente().calcula(cliente, valorItens);
    }

    public List<PoliticaDescontoInfo> listaPoliticas() {
        String codigoCorrente = descontoCorrenteRepository.recuperaCodigoDescontoCorrente();
        return politicas.values().stream()
            .map(politica -> new PoliticaDescontoInfo(
                politica.codigo(),
                politica.descricao(),
                politica.codigo().equals(codigoCorrente)))
            .toList();
    }

    public void definePoliticaCorrente(String codigo) {
        if (!politicas.containsKey(codigo)) {
            throw new IllegalArgumentException("Politica de desconto nao encontrada: " + codigo);
        }
        descontoCorrenteRepository.defineCodigoDescontoCorrente(codigo);
    }

    private PoliticaDesconto politicaCorrente() {
        String codigo = descontoCorrenteRepository.recuperaCodigoDescontoCorrente();
        PoliticaDesconto politica = politicas.get(codigo);
        if (politica == null) {
            throw new IllegalStateException("Politica de desconto corrente invalida: " + codigo);
        }
        return politica;
    }

    private void registraPoliticas() {
        registra(new PoliticaDesconto() {
            @Override
            public String codigo() {
                return "SEM_DESCONTO";
            }

            @Override
            public String descricao() {
                return "Sem desconto";
            }

            @Override
            public double calcula(Cliente cliente, double valorItens) {
                return 0;
            }
        });

        registra(new PoliticaDesconto() {
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
                return calculaFidelidade(cliente, valorItens);
            }
        });

        registra(new PoliticaDesconto() {
            @Override
            public String codigo() {
                return "PROMOCAO_10";
            }

            @Override
            public String descricao() {
                return "Promocao geral de 10%";
            }

            @Override
            public double calcula(Cliente cliente, double valorItens) {
                return valorItens * 0.10;
            }
        });
    }

    private void registra(PoliticaDesconto politica) {
        politicas.put(politica.codigo(), politica);
    }

    private double calculaFidelidade(Cliente cliente, double valorItens) {
        LocalDateTime vinteDiasAtras = LocalDateTime.now().minusDays(20);
        int pedidosRecentes = pedidosRepository.contaPedidosClienteDesde(cliente.getCpf(), vinteDiasAtras);
        if (pedidosRecentes > 3) {
            return valorItens * 0.07;
        }
        return 0;
    }
}
