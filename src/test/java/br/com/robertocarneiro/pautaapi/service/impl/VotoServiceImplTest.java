package br.com.robertocarneiro.pautaapi.service.impl;

import br.com.robertocarneiro.pautaapi.dtos.UserByCpfDTO;
import br.com.robertocarneiro.pautaapi.dtos.VotoCountDTO;
import br.com.robertocarneiro.pautaapi.dtos.VotoSaveDTO;
import br.com.robertocarneiro.pautaapi.entities.Associado;
import br.com.robertocarneiro.pautaapi.entities.Pauta;
import br.com.robertocarneiro.pautaapi.entities.Voto;
import br.com.robertocarneiro.pautaapi.enums.EnumBoolean;
import br.com.robertocarneiro.pautaapi.enums.VoteSituationStatus;
import br.com.robertocarneiro.pautaapi.exceptions.*;
import br.com.robertocarneiro.pautaapi.repositories.VotoRepository;
import br.com.robertocarneiro.pautaapi.restclients.UserServiceClient;
import br.com.robertocarneiro.pautaapi.services.AssociadoService;
import br.com.robertocarneiro.pautaapi.services.PautaService;
import br.com.robertocarneiro.pautaapi.services.impl.VotoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static br.com.robertocarneiro.pautaapi.services.impl.VotoServiceImpl.EMPATE;
import static br.com.robertocarneiro.pautaapi.services.impl.VotoServiceImpl.VOTACAO_AINDA_NAO_ABERTA;
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
    void shouldNotFindOneVotoByAssociadoAndPauta() {
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

    @Test
    void shouldFindOneVotoByAssociadoAndPauta() {
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

    @ParameterizedTest
    @MethodSource("providePautaIsNotOpened")
    void shouldThrowsVoteHasNotYetBeenOpenedExceptionWhenPautaIsNotOpened(LocalDateTime dataAbertura,
                                                                          LocalDateTime dataEncerramento) {
        VotoSaveDTO votoSaveDTO = VotoSaveDTO
                .builder()
                .associadoId(1L)
                .pautaId(1L)
                .resposta("teste")
                .build();
        Associado associado = Associado.builder().associadoId(votoSaveDTO.getAssociadoId()).cpf("cpf").build();
        Pauta pauta = Pauta
                .builder()
                .pautaId(votoSaveDTO.getPautaId())
                .dataAberturaVotacao(dataAbertura)
                .dataEncerramentoVotacao(dataEncerramento)
                .build();

        when(associadoService.findById(eq(votoSaveDTO.getAssociadoId()))).thenReturn(associado);
        when(pautaService.findById(eq(votoSaveDTO.getPautaId()))).thenReturn(pauta);

        assertThrows(VoteHasNotYetBeenOpenedException.class, () -> votoService.save(votoSaveDTO));
    }

    private static Stream<Arguments> providePautaIsNotOpened() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(LocalDateTime.now(), null),
                Arguments.of(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusHours(1)));
    }

    @Test
    void shouldThrowsVoteClosedExceptionWhenPautaIsClosedToVote() {
        VotoSaveDTO votoSaveDTO = VotoSaveDTO
                .builder()
                .associadoId(1L)
                .pautaId(1L)
                .resposta("teste")
                .build();
        Associado associado = Associado.builder().associadoId(votoSaveDTO.getAssociadoId()).cpf("cpf").build();
        Pauta pauta = Pauta
                .builder()
                .pautaId(votoSaveDTO.getPautaId())
                .dataAberturaVotacao(LocalDateTime.now().minusHours(1))
                .dataEncerramentoVotacao(LocalDateTime.now().minusMinutes(1))
                .build();

        when(associadoService.findById(eq(votoSaveDTO.getAssociadoId()))).thenReturn(associado);
        when(pautaService.findById(eq(votoSaveDTO.getPautaId()))).thenReturn(pauta);

        assertThrows(VoteClosedException.class, () -> votoService.save(votoSaveDTO));
    }

    @Test
    void shouldThrowsAlreadyVotedExceptionWhenAssociadoAlreadyVotedOnPauta() {
        VotoSaveDTO votoSaveDTO = VotoSaveDTO
                .builder()
                .associadoId(1L)
                .pautaId(1L)
                .resposta("teste")
                .build();
        Associado associado = Associado.builder().associadoId(votoSaveDTO.getAssociadoId()).cpf("cpf").build();
        Pauta pauta = Pauta
                .builder()
                .pautaId(votoSaveDTO.getPautaId())
                .dataAberturaVotacao(LocalDateTime.now().minusMinutes(1))
                .dataEncerramentoVotacao(LocalDateTime.now().plusHours(1))
                .build();
        Optional<Voto> votoOptional = Optional.of(Voto.builder().associado(associado).pauta(pauta).build());

        when(associadoService.findById(eq(votoSaveDTO.getAssociadoId()))).thenReturn(associado);
        when(pautaService.findById(eq(votoSaveDTO.getPautaId()))).thenReturn(pauta);
        when(repository.findFirstByAssociadoAndPauta(eq(associado), eq(pauta))).thenReturn(votoOptional);

        assertThrows(AlreadyVotedException.class, () -> votoService.save(votoSaveDTO));
    }

    @ParameterizedTest
    @MethodSource("provideUnableToVoteAndNull")
    void shouldThrowsExternalServiceDidNotAllowVoteExceptionWhenExternalServiceNotAllowCPF(VoteSituationStatus status) {
        VotoSaveDTO votoSaveDTO = VotoSaveDTO
                .builder()
                .associadoId(1L)
                .pautaId(1L)
                .resposta("teste")
                .build();
        Associado associado = Associado.builder().associadoId(votoSaveDTO.getAssociadoId()).cpf("cpf").build();
        Pauta pauta = Pauta
                .builder()
                .pautaId(votoSaveDTO.getPautaId())
                .dataAberturaVotacao(LocalDateTime.now().minusMinutes(1))
                .dataEncerramentoVotacao(LocalDateTime.now().plusHours(1))
                .build();
        Optional<Voto> votoOptional = Optional.empty();
        UserByCpfDTO userByCpf = UserByCpfDTO.builder().status(status).build();

        when(associadoService.findById(eq(votoSaveDTO.getAssociadoId()))).thenReturn(associado);
        when(pautaService.findById(eq(votoSaveDTO.getPautaId()))).thenReturn(pauta);
        when(repository.findFirstByAssociadoAndPauta(eq(associado), eq(pauta))).thenReturn(votoOptional);
        when(userServiceClient.findUserByCpf(eq(associado.getCpf()))).thenReturn(userByCpf);

        assertThrows(ExternalServiceDidNotAllowVoteException.class, () -> votoService.save(votoSaveDTO));
    }

    private static Stream<Arguments> provideUnableToVoteAndNull() {
        return Stream.of(
                Arguments.of((VoteSituationStatus) null),
                Arguments.of(VoteSituationStatus.UNABLE_TO_VOTE));
    }

    @Test
    void shouldThrowsInvalidVoteOptionExceptionWhenRespostaIsInvalid() {
        VotoSaveDTO votoSaveDTO = VotoSaveDTO
                .builder()
                .associadoId(1L)
                .pautaId(1L)
                .resposta("teste")
                .build();
        Associado associado = Associado.builder().associadoId(votoSaveDTO.getAssociadoId()).cpf("cpf").build();
        Pauta pauta = Pauta
                .builder()
                .pautaId(votoSaveDTO.getPautaId())
                .dataAberturaVotacao(LocalDateTime.now().minusMinutes(1))
                .dataEncerramentoVotacao(LocalDateTime.now().plusHours(1))
                .build();
        Optional<Voto> votoOptional = Optional.empty();
        UserByCpfDTO userByCpf = UserByCpfDTO.builder().status(VoteSituationStatus.ABLE_TO_VOTE).build();

        when(associadoService.findById(eq(votoSaveDTO.getAssociadoId()))).thenReturn(associado);
        when(pautaService.findById(eq(votoSaveDTO.getPautaId()))).thenReturn(pauta);
        when(repository.findFirstByAssociadoAndPauta(eq(associado), eq(pauta))).thenReturn(votoOptional);
        when(userServiceClient.findUserByCpf(eq(associado.getCpf()))).thenReturn(userByCpf);

        assertThrows(InvalidVoteOptionException.class, () -> votoService.save(votoSaveDTO));
    }

    @ParameterizedTest
    @EnumSource(EnumBoolean.class)
    void shouldSaveWhenVotoSaveDTOWithRespostaValid(EnumBoolean enumBoolean) {
        VotoSaveDTO votoSaveDTO = VotoSaveDTO
                .builder()
                .associadoId(1L)
                .pautaId(1L)
                .resposta(enumBoolean.getDescricao())
                .build();
        Associado associado = Associado.builder().associadoId(votoSaveDTO.getAssociadoId()).cpf("cpf").build();
        Pauta pauta = Pauta
                .builder()
                .pautaId(votoSaveDTO.getPautaId())
                .dataAberturaVotacao(LocalDateTime.now().minusMinutes(1))
                .dataEncerramentoVotacao(LocalDateTime.now().plusHours(1))
                .build();
        Optional<Voto> votoOptional = Optional.empty();
        UserByCpfDTO userByCpf = UserByCpfDTO.builder().status(VoteSituationStatus.ABLE_TO_VOTE).build();

        when(associadoService.findById(eq(votoSaveDTO.getAssociadoId()))).thenReturn(associado);
        when(pautaService.findById(eq(votoSaveDTO.getPautaId()))).thenReturn(pauta);
        when(repository.findFirstByAssociadoAndPauta(eq(associado), eq(pauta))).thenReturn(votoOptional);
        when(userServiceClient.findUserByCpf(eq(associado.getCpf()))).thenReturn(userByCpf);

        votoService.save(votoSaveDTO);

        ArgumentCaptor<Voto> votoArgumentCaptor = ArgumentCaptor.forClass(Voto.class);
        verify(associadoService, times(1)).findById(eq(associado.getAssociadoId()));
        verify(pautaService, times(1)).findById(eq(pauta.getPautaId()));
        verify(repository, times(1))
                .findFirstByAssociadoAndPauta(eq(associado), eq(pauta));
        verify(userServiceClient, times(1)).findUserByCpf(eq(associado.getCpf()));
        verify(repository, times(1)).save(votoArgumentCaptor.capture());

        Voto votoSaved = votoArgumentCaptor.getValue();
        assertNotNull(votoSaved);
        assertEquals(associado, votoSaved.getAssociado());
        assertEquals(pauta, votoSaved.getPauta());
        assertEquals(enumBoolean, votoSaved.getResposta());
    }

    @ParameterizedTest
    @MethodSource("provideVoteSituation")
    void shouldReturnVotacaoAindaNaoAbertaWhenZeroVotoAndPautaIsNotOpened(LocalDateTime dataAbertura,
                                                                          LocalDateTime dataEncerramento,
                                                                          List<Voto> votos,
                                                                          String mostVoted) {
        Pauta pauta = Pauta
                .builder()
                .dataAberturaVotacao(dataAbertura)
                .dataEncerramentoVotacao(dataEncerramento)
                .build();

        when(repository.findAllByPauta(eq(pauta))).thenReturn(votos);

        VotoCountDTO votoCountDTO = votoService.voteCount(pauta);

        assertNotNull(votoCountDTO);
        assertEquals(mostVoted, votoCountDTO.getMostVoted());
    }

    private static Stream<Arguments> provideVoteSituation() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
                Arguments.of(null, null, List.of(), VOTACAO_AINDA_NAO_ABERTA),
                Arguments.of(now, null, List.of(), VOTACAO_AINDA_NAO_ABERTA),
                Arguments.of(now.plusMinutes(1), now.plusHours(1),
                        List.of(), VOTACAO_AINDA_NAO_ABERTA),
                Arguments.of(now.minusMinutes(1), now.plusHours(1),
                        List.of(), EMPATE),
                Arguments.of(now.minusMinutes(1), now.plusHours(1),
                        List.of(Voto.builder().resposta(EnumBoolean.SIM).build()), EnumBoolean.SIM.getDescricao()),
                Arguments.of(now.minusMinutes(1), now.plusHours(1),
                        List.of(Voto.builder().resposta(EnumBoolean.NAO).build()), EnumBoolean.NAO.getDescricao()));
    }

}
