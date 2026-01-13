package com.validacaoemail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request de login")
public class LoginRequest {

    @NotBlank(message = "Username não pode ser vazio")
    @Schema(description = "Username do administrador", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank(message = "Password não pode ser vazio")
    @Schema(description = "Senha do administrador", example = "admin123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
