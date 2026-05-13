package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SolicitarStatusPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoParaAprovacaoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.StatusPedidoResponse;

@RestController
@RequestMapping("/pedidos")
public class PedidosController {
    private SubmeterPedidoParaAprovacaoUC submeterPedidoParaAprovacaoUC;
    private SolicitarStatusPedidoUC solicitarStatusPedidoUC;

    public PedidosController(SubmeterPedidoParaAprovacaoUC submeterPedidoParaAprovacaoUC,
                             SolicitarStatusPedidoUC solicitarStatusPedidoUC) {
        this.submeterPedidoParaAprovacaoUC = submeterPedidoParaAprovacaoUC;
        this.solicitarStatusPedidoUC = solicitarStatusPedidoUC;
    }

    @PostMapping("/aprovacao")
    @CrossOrigin("*")
    public PedidoResponse submeteParaAprovacao(@RequestBody SubmeterPedidoRequest request) {
        return submeterPedidoParaAprovacaoUC.run(request);
    }

    @GetMapping("/{id}/status")
    @CrossOrigin("*")
    public StatusPedidoResponse solicitaStatus(@PathVariable(value = "id") long idPedido) {
        return solicitarStatusPedidoUC.run(idPedido);
    }
}
