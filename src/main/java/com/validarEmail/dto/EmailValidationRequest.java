package com.validarEmail.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailValidationRequest {

    @NotBlank(message = "Email não pode ser vazio")
    @Email(message = "Formato de email inválido")
    private String email;
}
