package com.backendjava.simulacao.dto;

import java.util.List;

public record ErrorResponse(String mensagem, List<String> detalhes) {
}

