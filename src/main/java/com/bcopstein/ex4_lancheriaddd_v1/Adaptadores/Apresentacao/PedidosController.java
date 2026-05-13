package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoParaAprovacaoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;

@RestController
@RequestMapping("/pedidos")
public class PedidosController {
    private SubmeterPedidoParaAprovacaoUC submeterPedidoParaAprovacaoUC;

    public PedidosController(SubmeterPedidoParaAprovacaoUC submeterPedidoParaAprovacaoUC) {
        this.submeterPedidoParaAprovacaoUC = submeterPedidoParaAprovacaoUC;
    }

    @PostMapping("/aprovacao")
    @CrossOrigin("*")
    public PedidoResponse submeteParaAprovacao(@RequestBody SubmeterPedidoRequest request) {
        return submeterPedidoParaAprovacaoUC.run(request);
    }
}
