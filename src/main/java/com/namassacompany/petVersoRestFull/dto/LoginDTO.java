package com.namassacompany.petVersoRestFull.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank String login,
        @NotBlank String senha
) {
}
