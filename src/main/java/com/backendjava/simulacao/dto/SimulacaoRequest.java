package com.backendjava.simulacao.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record SimulacaoRequest(
        @NotNull(message = "valorInicial e obrigatorio")
        @DecimalMin(value = "0.01", message = "valorInicial deve ser maior que zero")
        @Digits(integer = 16, fraction = 2, message = "valorInicial deve ter no maximo 2 casas decimais")
        BigDecimal valorInicial,

        @NotNull(message = "taxaJurosMensal e obrigatoria")
        @DecimalMin(value = "0.00", inclusive = false, message = "taxaJurosMensal deve ser maior que zero")
        @Digits(integer = 6, fraction = 4, message = "taxaJurosMensal invalida")
        BigDecimal taxaJurosMensal,

        @NotNull(message = "prazoMeses e obrigatorio")
        @Min(value = 1, message = "prazoMeses deve ser no minimo 1")
        @Max(value = 600, message = "prazoMeses deve ser no maximo 600")
        Integer prazoMeses
) {
}

