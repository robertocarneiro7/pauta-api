package br.com.robertocarneiro.pautaapi.services.impl;

import br.com.robertocarneiro.pautaapi.dtos.UserByCpfDTO;
import br.com.robertocarneiro.pautaapi.dtos.VotoCountDTO;
import br.com.robertocarneiro.pautaapi.dtos.VotoSaveDTO;
import br.com.robertocarneiro.pautaapi.entities.Associado;
import br.com.robertocarneiro.pautaapi.entities.Pauta;
import br.com.robertocarneiro.pautaapi.entities.Voto;
import br.com.robertocarneiro.pautaapi.enums.EnumBoolean;
import br.com.robertocarneiro.pautaapi.enums.VoteSituationStatus;
import br.com.robertocarneiro.pautaapi.exceptions.AlreadyVotedException;
import br.com.robertocarneiro.pautaapi.exceptions.ExternalServiceDidNotAllowVoteException;
import br.com.robertocarneiro.pautaapi.exceptions.VoteClosedException;
import br.com.robertocarneiro.pautaapi.exceptions.VoteHasNotYetBeenOpenedException;
import br.com.robertocarneiro.pautaapi.repositories.VotoRepository;
import br.com.robertocarneiro.pautaapi.restclients.UserServiceClient;
import br.com.robertocarneiro.pautaapi.services.AssociadoService;
import br.com.robertocarneiro.pautaapi.services.PautaService;
import br.com.robertocarneiro.pautaapi.services.VotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class VotoServiceImpl implements VotoService {

    private final VotoRepository repository;
    private final PautaService pautaService;
    private final AssociadoService associadoService;
    private final UserServiceClient userServiceClient;

    public static final String EMPATE = "EMPATE";
    public static final String VOTACAO_AINDA_NAO_ABERTA = "VOTAÇÃO AINDA NÃO FOI ABERTA";

    @Override
    public Optional<Voto> findFirstByAssociadoAndPauta(Associado associado, Pauta pauta) {
        return repository.findFirstByAssociadoAndPauta(associado, pauta);
    }

    @Override
    public void save(VotoSaveDTO votoSaveDTO) {
        Associado associado = associadoService.findById(votoSaveDTO.getAssociadoId());
        Pauta pauta = pautaService.findById(votoSaveDTO.getPautaId());
        validateIfCanVote(associado, pauta);

        UserByCpfDTO userByCpf = userServiceClient.findUserByCpf(associado.getCpf());
        if (!VoteSituationStatus.ABLE_TO_VOTE.equals(userByCpf.getStatus())) {
            throw new ExternalServiceDidNotAllowVoteException(associado.getCpf());
        }

        Voto voto = Voto
                .builder()
                .resposta(EnumBoolean.findByDescricao(votoSaveDTO.getResposta()))
                .associado(associado)
                .pauta(pauta)
                .build();
        repository.save(voto);
    }

    @Override
    public VotoCountDTO voteCount(Pauta pauta) {
        LocalDateTime now = LocalDateTime.now();
        List<Voto> votosByPauta = repository.findAllByPauta(pauta);
        long countOptionYes = votosByPauta.stream().filter(v -> EnumBoolean.SIM.equals(v.getResposta())).count();
        long countOptionNo = votosByPauta.size() - countOptionYes;
        String mostVoted = EMPATE;
        if (isNull(pauta.getDataAberturaVotacao()) || isNull(pauta.getDataEncerramentoVotacao())
                || now.isBefore(pauta.getDataAberturaVotacao())) {
            mostVoted = VOTACAO_AINDA_NAO_ABERTA;
        } else if (countOptionYes > countOptionNo) {
            mostVoted = EnumBoolean.SIM.getDescricao();
        } else if (countOptionYes < countOptionNo) {
            mostVoted = EnumBoolean.NAO.getDescricao();
        }
        return VotoCountDTO
                .builder()
                .countOptionYes(countOptionYes)
                .countOptionNo(countOptionNo)
                .mostVoted(mostVoted)
                .build();
    }

    @Override
    public void validateIfCanVote(Associado associado, Pauta pauta) {
        LocalDateTime now = LocalDateTime.now();
        if (isNull(pauta.getDataAberturaVotacao()) || isNull(pauta.getDataEncerramentoVotacao())
                || now.isBefore(pauta.getDataAberturaVotacao())) {
            throw new VoteHasNotYetBeenOpenedException(pauta.getPautaId());
        }
        if (now.isAfter(pauta.getDataEncerramentoVotacao())) {
            throw new VoteClosedException(pauta.getPautaId());
        }
        if (findFirstByAssociadoAndPauta(associado, pauta).isPresent()) {
            throw new AlreadyVotedException(Associado.class, associado.getAssociadoId(), pauta.getPautaId());
        }
    }
}
