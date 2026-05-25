package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;
 
public interface IPagamentoService {
    boolean processaPagamento(long idPedido, double valor);
}