package com.namassacompany.petVersoRestFull.dto;

public record CadastroResponseDTO (
        Long idUsuario,
        String nomeDeUsuario,
        String email,
        String token
){ }
