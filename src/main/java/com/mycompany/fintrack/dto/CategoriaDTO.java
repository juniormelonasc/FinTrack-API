package com.mycompany.fintrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados de uma categoria")
public class CategoriaDTO {

    @Schema(description = "ID da categoria", example = "1")
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome da categoria", example = "Alimentação")
    private String nome;
}