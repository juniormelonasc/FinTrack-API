package com.mycompany.fintrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Relatório de saldo do usuário")
public class SaldoDTO {

    @Schema(description = "Saldo total (receitas - despesas)", example = "1500.00")
    private BigDecimal saldo;

    @Schema(description = "Total de receitas", example = "3000.00")
    private BigDecimal totalReceitas;

    @Schema(description = "Total de despesas", example = "1500.00")
    private BigDecimal totalDespesas;
}