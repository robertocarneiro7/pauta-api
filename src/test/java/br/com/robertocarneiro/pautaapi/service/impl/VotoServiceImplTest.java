package br.com.robertocarneiro.pautaapi.service.impl;

import br.com.robertocarneiro.pautaapi.entities.Associado;
import br.com.robertocarneiro.pautaapi.entities.Pauta;
import br.com.robertocarneiro.pautaapi.entities.Voto;
import br.com.robertocarneiro.pautaapi.repositories.VotoRepository;
import br.com.robertocarneiro.pautaapi.restclients.UserServiceClient;
import br.com.robertocarneiro.pautaapi.services.AssociadoService;
import br.com.robertocarneiro.pautaapi.services.PautaService;
import br.com.robertocarneiro.pautaapi.services.impl.VotoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotoServiceImplTest {

    @InjectMocks
    private VotoServiceImpl votoService;

    @Mock
    private VotoRepository repository;

    @Mock
    private PautaService pautaService;

    @Mock
    private AssociadoService associadoService;

    @Mock
    private UserServiceClient userServiceClient;

    @Test
    void findOneVotoByAssociadoAndPauta() {
        Associado associado = Associado.builder().build();
        Pauta pauta = Pauta.builder().build();
        Optional<Voto> votoOptional = Optional.of(Voto.builder().build());

        when(repository.findFirstByAssociadoAndPauta(eq(associado), eq(pauta))).thenReturn(votoOptional);

        Optional<Voto> votoRetornoOptional = votoService.findFirstByAssociadoAndPauta(associado, pauta);

        verify(repository, times(1)).findFirstByAssociadoAndPauta(eq(associado), eq(pauta));
        assertNotNull(votoRetornoOptional);
        assertEquals(votoOptional, votoRetornoOptional);
        assertEquals(votoOptional.get(), votoRetornoOptional.orElse(null));
    }

    @Test
    void notFindOneVotoByAssociadoAndPauta() {
        Associado associado = Associado.builder().build();
        Pauta pauta = Pauta.builder().build();
        Optional<Voto> votoOptional = Optional.empty();

        when(repository.findFirstByAssociadoAndPauta(eq(associado), eq(pauta))).thenReturn(votoOptional);

        Optional<Voto> votoRetornoOptional = votoService.findFirstByAssociadoAndPauta(associado, pauta);

        verify(repository, times(1)).findFirstByAssociadoAndPauta(eq(associado), eq(pauta));
        assertNotNull(votoRetornoOptional);
        assertEquals(votoOptional, votoRetornoOptional);
        assertTrue(votoRetornoOptional.isEmpty());
    }
}
