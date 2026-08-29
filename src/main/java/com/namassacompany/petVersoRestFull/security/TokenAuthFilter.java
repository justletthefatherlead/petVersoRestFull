package com.namassacompany.petVersoRestFull.security;

import com.namassacompany.petVersoRestFull.model.Sessao;
import com.namassacompany.petVersoRestFull.model.Usuario;
import com.namassacompany.petVersoRestFull.repository.SessaoRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class TokenAuthFilter extends OncePerRequestFilter {
    private final SessaoRepository sessaoRepository;
    public TokenAuthFilter(SessaoRepository sessaoRepository) {
        this.sessaoRepository = sessaoRepository;
    }
    @Override
    protected  void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
    FilterChain filterChain) throws ServletException, IOException{
    String authHeader = request.getHeader("Authorization");
    if(authHeader != null && authHeader.startsWith("Bearer ")  ){
    String token = authHeader.substring(7);
        Optional<Sessao> sessao = sessaoRepository.findByToken(token);
        if (sessao.isPresent()){
            Sessao sessaoEncontrada = sessao.get();
            Usuario usuario  = sessaoEncontrada.getUsuario();
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(usuario, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
    }
    filterChain.doFilter(request, response);
    }
}
