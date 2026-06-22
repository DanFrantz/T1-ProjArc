package com.bcopstein.gateway.auth;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import reactor.core.publisher.Mono;

@Service
public class AuthService {
    private final WebClient webClient;
    private final JwtService jwtService;

    public AuthService(WebClient.Builder webClientBuilder, JwtService jwtService) {
        this.webClient = webClientBuilder.baseUrl("http://pizzaria-service").build();
        this.jwtService = jwtService;
    }

    public Mono<AuthResponse> login(AuthRequest request) {
        return webClient.post()
            .uri("/internal/auth/validate")
            .bodyValue(request)
            .retrieve()
            .onStatus(HttpStatusCode::isError, response ->
                Mono.error(new ResponseStatusException(HttpStatusCode.valueOf(401), "Email ou senha invalidos")))
            .bodyToMono(CredenciaisClienteResponse.class)
            .map(cliente -> new AuthResponse(
                jwtService.geraToken(cliente.email()),
                cliente.email(),
                cliente.nome()));
    }
}
