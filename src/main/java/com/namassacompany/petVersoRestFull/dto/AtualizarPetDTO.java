package com.namassacompany.petVersoRestFull.dto;

import java.util.List;

public record AtualizarPetDTO(
        String perfilDeSensibilidade,
        List<String> personalidades
) {
}
