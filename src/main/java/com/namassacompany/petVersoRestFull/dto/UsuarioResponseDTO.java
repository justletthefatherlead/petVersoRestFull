package com.namassacompany.petVersoRestFull.dto;

import com.namassacompany.petVersoRestFull.model.Usuario;

public record UsuarioResponseDTO(
        Long idUsuario,
        String nome,
        String email
) {
public UsuarioResponseDTO(Usuario usuario){
    this(usuario.getIdUsuario(), usuario.getNome(), usuario.getEmail());
}
}
