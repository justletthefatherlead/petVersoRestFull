package com.namassacompany.petVersoRestFull.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioCadastroDTO(
        @NotBlank(message = "O nome de usuario é obrigatorio") String nome,
        @NotBlank(message = "O Campo do email nao pode ficar em branco") String email,
        String telefone,
        @Size(min = 6) String senha
) {}
