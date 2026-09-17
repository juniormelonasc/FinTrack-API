package com.mycompany.fintrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados de uma transação")
public class TransacaoDTO {

    @Schema(description = "ID da transação", example = "1")
    private Long id;

    @NotBlank(message = "Descrição é obrigatória")
    @Schema(description = "Descrição", example = "Salário")
    private String descricao;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    @Schema(description = "Valor", example = "2500.00")
    private BigDecimal valor;

    @NotNull(message = "Tipo é obrigatório")
    @Schema(description = "Tipo (RECEITA ou DESPESA)", example = "RECEITA")
    private String tipo;

    @NotNull(message = "Data é obrigatória")
    @Schema(description = "Data", example = "2025-01-15")
    private LocalDate data;

    @Schema(description = "ID da categoria", example = "1")
    private Long categoriaId;
}