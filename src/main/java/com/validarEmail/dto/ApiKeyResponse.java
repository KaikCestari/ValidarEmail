package com.validarEmail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response da API Key")
public class ApiKeyResponse {

    @Schema(description = "ID da API Key")
    private Long id;

    @Schema(description = "Nome da API Key")
    private String name;

    @Schema(description = "API Key (apenas na criação)")
    private String key;

    @Schema(description = "Se está ativa")
    private Boolean active;

    @Schema(description = "Data de criação")
    private LocalDateTime createdAt;
}
