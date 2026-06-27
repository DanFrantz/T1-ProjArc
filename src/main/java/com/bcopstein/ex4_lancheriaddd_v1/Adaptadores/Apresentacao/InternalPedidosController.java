package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.AtualizarStatusPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@RestController
@RequestMapping("/internal/pedidos")
public class InternalPedidosController {
    private PedidosRepository pedidosRepository;

    public InternalPedidosController(PedidosRepository pedidosRepository) {
        this.pedidosRepository = pedidosRepository;
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizaStatus(@PathVariable("id") long idPedido,
                               @RequestBody AtualizarStatusPedidoRequest request) {
        pedidosRepository.atualizaStatus(idPedido, Pedido.Status.valueOf(request.status()));
    }
}
