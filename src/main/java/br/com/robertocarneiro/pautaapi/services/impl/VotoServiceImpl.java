package br.com.robertocarneiro.pautaapi.services.impl;

import br.com.robertocarneiro.pautaapi.dtos.VotoCountDTO;
import br.com.robertocarneiro.pautaapi.dtos.VotoSaveDTO;
import br.com.robertocarneiro.pautaapi.entities.Associado;
import br.com.robertocarneiro.pautaapi.entities.Pauta;
import br.com.robertocarneiro.pautaapi.entities.Voto;
import br.com.robertocarneiro.pautaapi.enums.EnumBoolean;
import br.com.robertocarneiro.pautaapi.exceptions.AlreadyVotedException;
import br.com.robertocarneiro.pautaapi.exceptions.VoteClosedException;
import br.com.robertocarneiro.pautaapi.exceptions.VoteHasNotYetBeenOpenedException;
import br.com.robertocarneiro.pautaapi.repositories.VotoRepository;
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

    private static final String EMPATE = "EMPATE";

    @Override
    public Optional<Voto> findFirstByAssociadoAndPauta(Associado associado, Pauta pauta) {
        return repository.findFirstByAssociadoAndPauta(associado, pauta);
    }

    @Override
    public void save(VotoSaveDTO votoSaveDTO, Long pautaId, Long associadoId) {
        Associado associado = associadoService.findById(associadoId);
        Pauta pauta = pautaService.findById(pautaId);
        validateIfCanVote(associado, pauta);

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
        List<Voto> votosByPauta = repository.findAllByPauta(pauta);
        long countOptionYes = votosByPauta.stream().filter(v -> EnumBoolean.SIM.equals(v.getResposta())).count();
        long countOptionNo = votosByPauta.size() - countOptionYes;
        String mostVoted = EMPATE;
        if (countOptionYes > countOptionNo) {
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
