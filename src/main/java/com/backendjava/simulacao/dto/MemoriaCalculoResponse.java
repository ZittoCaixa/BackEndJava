package com.backendjava.simulacao.dto;

import java.math.BigDecimal;

public record MemoriaCalculoResponse(
        Integer mes,
        BigDecimal saldoInicial,
        BigDecimal jurosMes,
        BigDecimal saldoFinal
) {
}

