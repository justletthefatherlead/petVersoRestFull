package com.namassacompany.petVersoRestFull.dto;

import com.namassacompany.petVersoRestFull.model.Porte;
import com.namassacompany.petVersoRestFull.model.Sexo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PetCadastroDTO(
        @NotBlank(message = "o campo nome é obrigatório") String nome,
        @NotBlank(message = "o campo raca é obrigatório") String raca,
        @NotBlank(message = "o campo especie é obrigatório") String especie,
        @NotBlank(message = "pode ser a data de adoção, caso nao saiba a data") LocalDateTime dataDeNascimento,
        @NotNull Porte porte,
        @NotNull(message = "se nao souber informe um peso aproximado") Double peso,
        @NotNull Sexo sexo
) {}
