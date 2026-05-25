package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Seguranca;
 
import java.io.IOException;
 
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
 
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.JwtService;
 
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
 
@Component
public class JwtFiltro extends OncePerRequestFilter {
    private JwtService jwtService;
 
    public JwtFiltro(JwtService jwtService) {
        this.jwtService = jwtService;
    }
 
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
 
        String authHeader = request.getHeader("Authorization");
 
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtService.tokenValido(token)) {
                String email = jwtService.extraiEmail(token);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null, java.util.List.of());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
 
        filterChain.doFilter(request, response);
    }
}
