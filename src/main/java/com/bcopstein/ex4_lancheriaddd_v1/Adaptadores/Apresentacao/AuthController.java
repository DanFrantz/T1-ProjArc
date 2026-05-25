package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;
 
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.AutenticarClienteUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.AutenticarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.AutenticarClienteResponse;
 
@RestController
@RequestMapping("/auth")
public record AuthController(AutenticarClienteUC autenticarClienteUC) {
 
    @PostMapping("/login")
    @CrossOrigin("*")
    public AutenticarClienteResponse login(@RequestBody AutenticarClienteRequest request) {
        return autenticarClienteUC.run(request);
    }
}