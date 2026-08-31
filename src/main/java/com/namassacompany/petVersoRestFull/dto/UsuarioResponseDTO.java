package com.namassacompany.petVersoRestFull.dto;

import com.namassacompany.petVersoRestFull.model.Usuario;

public record UsuarioResponseDTO(
        Long idUsuario,
        String nome,
        String apelido,
        String email
) {
public UsuarioResponseDTO(Usuario usuario){
    this(usuario.getIdUsuario(), usuario.getNome(), usuario.getApelido(), usuario.getEmail());
}
}
