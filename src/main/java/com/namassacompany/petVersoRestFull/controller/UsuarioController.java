package com.namassacompany.petVersoRestFull.controller;

import com.namassacompany.petVersoRestFull.dto.CadastroResponseDTO;
import com.namassacompany.petVersoRestFull.dto.UsuarioCadastroDTO;
import com.namassacompany.petVersoRestFull.dto.UsuarioResponseDTO;
import com.namassacompany.petVersoRestFull.model.Usuario;
import com.namassacompany.petVersoRestFull.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioController {

   private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/api/usuarios")
    public ResponseEntity<CadastroResponseDTO> cadastrar(@Valid @RequestBody UsuarioCadastroDTO usuario){
        CadastroResponseDTO novoUsuario = usuarioService.cadastrar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(novoUsuario);
    }
}
