package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;
 
import java.time.LocalDateTime;
 
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
 
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ListarPedidosClienteUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ListarPedidosEntreguesUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidosEntreguesResponse;
 
@RestController
@RequestMapping("/relatorios")
public record RelatoriosController(ListarPedidosEntreguesUC listarPedidosEntreguesUC, ListarPedidosClienteUC listarPedidosClienteUC) {
 
    @GetMapping("/pedidos/entregues")
    @CrossOrigin("*")
    public PedidosEntreguesResponse listarEntregues(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return listarPedidosEntreguesUC.run(inicio, fim);
    }
 
    @GetMapping("/pedidos/cliente")
    @CrossOrigin("*")
    public PedidosEntreguesResponse listarDoCliente(
            @RequestParam String email,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return listarPedidosClienteUC.run(email, inicio, fim);
    }
}

