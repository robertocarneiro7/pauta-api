package br.com.robertocarneiro.pautaapi.services.impl;

import br.com.robertocarneiro.pautaapi.entities.Associado;
import br.com.robertocarneiro.pautaapi.entities.Pauta;
import br.com.robertocarneiro.pautaapi.entities.Voto;
import br.com.robertocarneiro.pautaapi.exceptions.AlreadyVotedException;
import br.com.robertocarneiro.pautaapi.exceptions.VoteClosedException;
import br.com.robertocarneiro.pautaapi.exceptions.VoteHasNotYetBeenOpenedException;
import br.com.robertocarneiro.pautaapi.repositories.VotoRepository;
import br.com.robertocarneiro.pautaapi.services.VotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class VotoServiceImpl implements VotoService {

    private final VotoRepository repository;

    @Override
    public Optional<Voto> findFirstByAssociadoAndPauta(Associado associado, Pauta pauta) {
        return repository.findFirstByAssociadoAndPauta(associado, pauta);
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
