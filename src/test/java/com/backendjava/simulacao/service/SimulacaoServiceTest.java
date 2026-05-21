package com.backendjava.simulacao.service;

import com.backendjava.simulacao.dto.SimulacaoRequest;
import com.backendjava.simulacao.dto.SimulacaoResponse;
import com.backendjava.simulacao.entity.Simulacao;
import com.backendjava.simulacao.repository.SimulacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class SimulacaoServiceTest {

    @Test
    void deveCalcularMemoriaEValoresTotais() {
        SimulacaoRepository repository = Mockito.mock(SimulacaoRepository.class);
        SimulacaoService service = new SimulacaoService(repository);

        SimulacaoRequest request = new SimulacaoRequest(new BigDecimal("1000.00"), new BigDecimal("1.5"), 3);
        SimulacaoResponse response = service.simular(request);

        Assertions.assertEquals(3, response.memoriaCalculo().size());
        Assertions.assertEquals(new BigDecimal("1045.68"), response.valorTotalFinal());
        Assertions.assertEquals(new BigDecimal("45.68"), response.valorTotalJuros());

        ArgumentCaptor<Simulacao> captor = ArgumentCaptor.forClass(Simulacao.class);
        Mockito.verify(repository).persistAndFlush(captor.capture());
        Assertions.assertEquals(3, captor.getValue().getMemoriaCalculo().size());
    }

    @Test
    void deveRetornarErroQuandoIdNaoExiste() {
        SimulacaoRepository repository = Mockito.mock(SimulacaoRepository.class);
        Mockito.when(repository.findByIdOptional(99L)).thenReturn(Optional.empty());

        SimulacaoService service = new SimulacaoService(repository);

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.buscarPorId(99L));
    }
}
