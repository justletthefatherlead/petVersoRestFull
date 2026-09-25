package com.namassacompany.petVersoRestFull.dto;

import com.namassacompany.petVersoRestFull.model.Papel;

public record PetResumoDTO(
        Long idPet,
        String nome,
        String codigoVinculo,
        String papel
) {
}
