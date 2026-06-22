package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ImpostosService {
    private String leiCorrente;
    private Map<String, RegraImposto> regras;

    public ImpostosService(@Value("${pizzaria.imposto.lei:${PIZZARIA_IMPOSTO_LEI:LEI_10}}") String leiCorrente) {
        this.leiCorrente = leiCorrente;
        this.regras = Map.of(
            "LEI_10", new RegraImposto() {
                @Override
                public String lei() {
                    return "LEI_10";
                }

                @Override
                public double calcula(double valorItens) {
                    return valorItens * 0.10;
                }
            },
            "LEI_12", new RegraImposto() {
                @Override
                public String lei() {
                    return "LEI_12";
                }

                @Override
                public double calcula(double valorItens) {
                    return valorItens * 0.12;
                }
            });
    }

    public double calcula(double valorItens) {
        RegraImposto regra = regras.get(leiCorrente);
        if (regra == null) {
            throw new IllegalStateException("Lei de imposto nao configurada: " + leiCorrente);
        }
        return regra.calcula(valorItens);
    }
}
