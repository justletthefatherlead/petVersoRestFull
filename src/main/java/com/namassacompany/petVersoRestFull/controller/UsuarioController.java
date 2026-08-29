package com.namassacompany.petVersoRestFull.controller;

import com.namassacompany.petVersoRestFull.dto.CadastroResponseDTO;
import com.namassacompany.petVersoRestFull.dto.UsuarioCadastroDTO;
import com.namassacompany.petVersoRestFull.dto.UsuarioResponseDTO;
import com.namassacompany.petVersoRestFull.model.Usuario;
import com.namassacompany.petVersoRestFull.security.TokenAuthFilter;
import com.namassacompany.petVersoRestFull.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

   private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;

    }

    @PostMapping("/cadastrar")
    public ResponseEntity<CadastroResponseDTO> cadastrar(@Valid @RequestBody UsuarioCadastroDTO usuario){
        CadastroResponseDTO novoUsuario = usuarioService.cadastrar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(novoUsuario);
    }
@GetMapping("/perfil")
    public ResponseEntity<UsuarioResponseDTO> perfilUsuario(){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(new UsuarioResponseDTO(usuario));

    }
}
