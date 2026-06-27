package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.DescontoCorrenteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@Service
public class DescontosService {
    private DescontoCorrenteRepository descontoCorrenteRepository;
    private Map<String, PoliticaDesconto> politicas;

    public DescontosService(DescontoCorrenteRepository descontoCorrenteRepository,
                            List<PoliticaDesconto> politicasDisponiveis) {
        this.descontoCorrenteRepository = descontoCorrenteRepository;
        this.politicas = new LinkedHashMap<>();
        politicasDisponiveis.forEach(politica -> this.politicas.put(politica.codigo(), politica));
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

}
