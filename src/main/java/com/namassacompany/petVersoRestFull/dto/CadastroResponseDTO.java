package com.namassacompany.petVersoRestFull.dto;

public record CadastroResponseDTO (
        Long idUsuario,
        String nome,
        String email,
        String token
){ }
