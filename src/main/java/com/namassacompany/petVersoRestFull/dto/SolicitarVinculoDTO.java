package com.namassacompany.petVersoRestFull.dto;

import jakarta.validation.constraints.NotBlank;

public record SolicitarVinculoDTO(
       @NotBlank String codigoVinculo
) {
}
