package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;
 
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.CancelarPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.PagarPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SolicitarStatusPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoParaAprovacaoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CancelamentoPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PagarPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.StatusPedidoResponse;
 
@RestController
@RequestMapping("/pedidos")
public record PedidosController(SubmeterPedidoParaAprovacaoUC submeterPedidoParaAprovacaoUC,
SolicitarStatusPedidoUC solicitarStatusPedidoUC,
CancelarPedidoUC cancelarPedidoUC,
PagarPedidoUC pagarPedidoUC) {
 
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
 
    @DeleteMapping("/{id}")
    @CrossOrigin("*")
    public CancelamentoPedidoResponse cancelaPedido(@PathVariable(value = "id") long idPedido) {
        return cancelarPedidoUC.run(idPedido);
    }
 
    @PostMapping("/{id}/pagamento")
    @CrossOrigin("*")
    public PagarPedidoResponse pagaPedido(@PathVariable(value = "id") long idPedido) {
        return pagarPedidoUC.run(idPedido);
    }
}
