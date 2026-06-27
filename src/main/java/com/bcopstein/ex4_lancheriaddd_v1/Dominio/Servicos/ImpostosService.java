package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ImpostosService {
    private String leiCorrente;
    private Map<String, RegraImposto> regras;

    public ImpostosService(@Value("${pizzaria.imposto.lei:${PIZZARIA_IMPOSTO_LEI:LEI_10}}") String leiCorrente,
                           List<RegraImposto> regrasDisponiveis) {
        this.leiCorrente = leiCorrente;
        this.regras = new LinkedHashMap<>();
        regrasDisponiveis.forEach(regra -> this.regras.put(regra.lei(), regra));
    }

    public double calcula(double valorItens) {
        RegraImposto regra = regras.get(leiCorrente);
        if (regra == null) {
            throw new IllegalStateException("Lei de imposto nao configurada: " + leiCorrente);
        }
        return regra.calcula(valorItens);
    }

    public List<ImpostoInfo> listaImpostos() {
        return regras.values().stream()
            .map(regra -> new ImpostoInfo(
                regra.lei(),
                regra.descricao(),
                regra.lei().equals(leiCorrente)))
            .toList();
    }
}
