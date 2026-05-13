package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

@Service
public class ImpostosService {
    public double calcula(double valorItens) {
        return valorItens * 0.10;
    }
}
