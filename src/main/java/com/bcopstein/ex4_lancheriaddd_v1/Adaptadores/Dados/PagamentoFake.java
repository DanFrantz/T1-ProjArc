package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;
 
import org.springframework.stereotype.Component;
 
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.IPagamentoService;
 
@Component
public class PagamentoFake implements IPagamentoService {//Essa é a versão que sempre aprova pagamentos

    @Override
    public boolean processaPagamento(long idPedido, double valor) {
        System.out.println("Pagamento aprovado para pedido " + idPedido + " no valor de R$ " + valor);
        return true;
    }
}
 