package com.backendjava.simulacao.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record SimulacaoResponse(
        Long id,
        BigDecimal valorInicial,
        BigDecimal taxaJurosMensal,
        Integer prazoMeses,
        BigDecimal valorTotalFinal,
        BigDecimal valorTotalJuros,
        OffsetDateTime criadoEm,
        List<MemoriaCalculoResponse> memoriaCalculo
) {
}

