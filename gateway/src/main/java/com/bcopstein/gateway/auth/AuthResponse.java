package com.bcopstein.gateway.auth;

public record AuthResponse(String token, String email, String nome) {
}
