package com.namassacompany.petVersoRestFull.dto;

import com.namassacompany.petVersoRestFull.model.Porte;
import com.namassacompany.petVersoRestFull.model.Sexo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PetCadastroDTO(
        @NotBlank(message = "o campo nome é obrigatório") String nome,
        @NotBlank(message = "o campo raca é obrigatório") String raca,
        @NotBlank(message = "o campo especie é obrigatório") String especie,
        @NotNull Porte porte,
        @NotNull(message = "se nao souber informe um peso aproximado") Double peso,
        @NotNull Sexo sexo
) {}
