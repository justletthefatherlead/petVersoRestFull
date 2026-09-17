package com.namassacompany.petVersoRestFull.dto;

import com.namassacompany.petVersoRestFull.model.Usuario;

import java.util.Base64;

public record UsuarioPerfilDTO(
        Long idUsuario,
        String nome,
        String apelido,
        String email,
        String telefone,
        String fotoBase64
) {
    public UsuarioPerfilDTO(Usuario usuario){
        this(usuario.getIdUsuario(), usuario.getNome(), usuario.getApelido(),usuario.getEmail(),usuario.getTelefone(),
                (usuario.getFoto() != null) ? Base64.getEncoder().encodeToString(usuario.getFoto()): null);
    }
}
