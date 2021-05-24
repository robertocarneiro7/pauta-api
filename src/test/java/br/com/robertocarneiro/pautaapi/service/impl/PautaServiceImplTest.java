package br.com.robertocarneiro.pautaapi.service.impl;

import br.com.robertocarneiro.pautaapi.dtos.PautaOpenVoteDTO;
import br.com.robertocarneiro.pautaapi.dtos.PautaSaveDTO;
import br.com.robertocarneiro.pautaapi.entities.Pauta;
import br.com.robertocarneiro.pautaapi.exceptions.EntityNotFoundException;
import br.com.robertocarneiro.pautaapi.exceptions.FieldCantBeRepeatException;
import br.com.robertocarneiro.pautaapi.exceptions.VoteAlreadyOpenException;
import br.com.robertocarneiro.pautaapi.mappers.PautaSaveDTOMapper;
import br.com.robertocarneiro.pautaapi.repositories.PautaRepository;
import br.com.robertocarneiro.pautaapi.services.impl.PautaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PautaServiceImplTest {

    @InjectMocks
    private PautaServiceImpl pautaService;

    @Mock
    private PautaRepository repository;

    @Mock
    private PautaSaveDTOMapper pautaSaveDTOMapper;

    @Test
    void shouldThrowsEntityNotFoundExceptionWhenNotFindOnePautaById() {
        Long pautaId = 1L;
        Optional<Pauta> pautaOptional = Optional.empty();

        when(repository.findById(eq(pautaId))).thenReturn(pautaOptional);

        assertThrows(EntityNotFoundException.class, () -> pautaService.findById(pautaId));
    }

    @Test
    void shouldReturnPautaWhenFindOnePautaById() {
        Long pautaId = 1L;
        Optional<Pauta> pautaOptional = Optional.of(Pauta.builder().pautaId(pautaId).build());

        when(repository.findById(eq(pautaId))).thenReturn(pautaOptional);

        Pauta pautaRetorno = pautaService.findById(pautaId);

        verify(repository, times(1)).findById(eq(pautaId));

        assertNotNull(pautaRetorno);
        assertEquals(pautaOptional.get(), pautaRetorno);
    }

    @Test
    void shouldThrowsFieldCantBeRepeatExceptionWhenAlreadyExistsOnDBPautaWithSameName() {
        PautaSaveDTO pautaSaveDTO = PautaSaveDTO.builder().nome("Nome").build();
        Pauta pauta = Pauta.builder().nome(pautaSaveDTO.getNome()).build();

        when(pautaSaveDTOMapper.dtoToEntity(eq(pautaSaveDTO))).thenReturn(pauta);
        when(repository.findFirstByNome(eq(pauta.getNome()))).thenReturn(Optional.of(pauta));

        assertThrows(FieldCantBeRepeatException.class, () -> pautaService.save(pautaSaveDTO));
    }

    @Test
    void shouldSavePautaWhenNotExistsOnDBPautaWithSameName() {
        PautaSaveDTO pautaSaveDTO = PautaSaveDTO.builder().nome("Nome").build();
        Pauta pauta = Pauta.builder().nome(pautaSaveDTO.getNome()).build();

        when(pautaSaveDTOMapper.dtoToEntity(eq(pautaSaveDTO))).thenReturn(pauta);
        when(repository.findFirstByNome(eq(pauta.getNome()))).thenReturn(Optional.empty());

        pautaService.save(pautaSaveDTO);

        verify(pautaSaveDTOMapper, times(1)).dtoToEntity(eq(pautaSaveDTO));
        verify(repository, times(1)).findFirstByNome(eq(pauta.getNome()));
        verify(repository, times(1)).save(eq(pauta));
    }

    @ParameterizedTest
    @MethodSource("provideSituationToCanNotOpenVote")
    void shouldThrowsVoteAlreadyOpenExceptionWhenPautaAlreadyIsOpenedToVote(LocalDateTime dataAbertura,
                                                                            LocalDateTime dataEncerramento) {
        PautaOpenVoteDTO pautaOpenVoteDTO = PautaOpenVoteDTO.builder().pautaId(1L).duracaoVotacao(1L).build();
        Pauta pauta = Pauta
                .builder()
                .pautaId(pautaOpenVoteDTO.getPautaId())
                .dataAberturaVotacao(dataAbertura)
                .dataEncerramentoVotacao(dataEncerramento)
                .build();

        when(repository.findById(eq(pautaOpenVoteDTO.getPautaId()))).thenReturn(Optional.of(pauta));

        assertThrows(VoteAlreadyOpenException.class, () -> pautaService.openVote(pautaOpenVoteDTO));
    }

    private static Stream<Arguments> provideSituationToCanNotOpenVote() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
                Arguments.of(now, null),
                Arguments.of(null, now),
                Arguments.of(now, now));
    }

    @Test
    void shouldOpenVoteWhenPautaIsNotOpenedToVote() {
        PautaOpenVoteDTO pautaOpenVoteDTO = PautaOpenVoteDTO.builder().pautaId(1L).duracaoVotacao(1L).build();
        Pauta pauta = Pauta.builder().pautaId(pautaOpenVoteDTO.getPautaId()).build();

        when(repository.findById(eq(pautaOpenVoteDTO.getPautaId()))).thenReturn(Optional.of(pauta));

        pautaService.openVote(pautaOpenVoteDTO);

        ArgumentCaptor<Pauta> pautaArgumentCaptor = ArgumentCaptor.forClass(Pauta.class);
        verify(repository, times(1)).findById(eq(pautaOpenVoteDTO.getPautaId()));
        verify(repository, times(1)).save(pautaArgumentCaptor.capture());

        Pauta pautaCaptured = pautaArgumentCaptor.getValue();
        assertNotNull(pautaCaptured);
        assertEquals(pauta, pautaCaptured);
        LocalDateTime dataEncerramentoExpected =
                pautaCaptured.getDataAberturaVotacao().plusMinutes(pautaOpenVoteDTO.getDuracaoVotacao());
        assertEquals(dataEncerramentoExpected, pautaCaptured.getDataEncerramentoVotacao());
    }

}
