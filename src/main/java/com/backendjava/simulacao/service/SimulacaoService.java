package com.backendjava.simulacao.service;

import com.backendjava.simulacao.dto.MemoriaCalculoResponse;
import com.backendjava.simulacao.dto.SimulacaoRequest;
import com.backendjava.simulacao.dto.SimulacaoResponse;
import com.backendjava.simulacao.entity.MemoriaCalculo;
import com.backendjava.simulacao.entity.Simulacao;
import com.backendjava.simulacao.repository.SimulacaoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SimulacaoService {

    private static final BigDecimal CEM = new BigDecimal("100");

    private final SimulacaoRepository simulacaoRepository;

    @Inject
    public SimulacaoService(SimulacaoRepository simulacaoRepository) {
        this.simulacaoRepository = simulacaoRepository;
    }

    @Transactional
    public SimulacaoResponse simular(SimulacaoRequest request) {
        BigDecimal taxaDecimal = request.taxaJurosMensal()
                .divide(CEM, 10, RoundingMode.HALF_UP);

        Simulacao simulacao = new Simulacao();
        simulacao.setValorInicial(request.valorInicial().setScale(2, RoundingMode.HALF_UP));
        simulacao.setTaxaJurosMensal(request.taxaJurosMensal().setScale(4, RoundingMode.HALF_UP));
        simulacao.setPrazoMeses(request.prazoMeses());
        simulacao.setCriadoEm(OffsetDateTime.now());

        List<MemoriaCalculo> memoria = new ArrayList<>();
        BigDecimal saldoAtual = simulacao.getValorInicial();

        for (int mes = 1; mes <= request.prazoMeses(); mes++) {
            BigDecimal jurosMes = saldoAtual.multiply(taxaDecimal).setScale(2, RoundingMode.HALF_UP);
            BigDecimal saldoFinal = saldoAtual.add(jurosMes).setScale(2, RoundingMode.HALF_UP);

            MemoriaCalculo item = new MemoriaCalculo();
            item.setMes(mes);
            item.setSaldoInicial(saldoAtual);
            item.setJurosMes(jurosMes);
            item.setSaldoFinal(saldoFinal);
            item.setSimulacao(simulacao);
            memoria.add(item);

            saldoAtual = saldoFinal;
        }

        simulacao.setMemoriaCalculo(memoria);
        simulacao.setValorTotalFinal(saldoAtual);
        simulacao.setValorTotalJuros(saldoAtual.subtract(simulacao.getValorInicial()).setScale(2, RoundingMode.HALF_UP));

        simulacaoRepository.persistAndFlush(simulacao);
        return toResponse(simulacao);
    }

    public SimulacaoResponse buscarPorId(Long id) {
        Simulacao simulacao = simulacaoRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Simulacao com id " + id + " nao encontrada"));
        return toResponse(simulacao);
    }

    private SimulacaoResponse toResponse(Simulacao simulacao) {
        List<MemoriaCalculoResponse> memoria = simulacao.getMemoriaCalculo().stream()
                .map(item -> new MemoriaCalculoResponse(
                        item.getMes(),
                        item.getSaldoInicial(),
                        item.getJurosMes(),
                        item.getSaldoFinal()))
                .toList();

        return new SimulacaoResponse(
                simulacao.getId(),
                simulacao.getValorInicial(),
                simulacao.getTaxaJurosMensal(),
                simulacao.getPrazoMeses(),
                simulacao.getValorTotalFinal(),
                simulacao.getValorTotalJuros(),
                simulacao.getCriadoEm(),
                memoria);
    }
}
