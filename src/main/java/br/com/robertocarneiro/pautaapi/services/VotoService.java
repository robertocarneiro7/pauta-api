package br.com.robertocarneiro.pautaapi.services;

import br.com.robertocarneiro.pautaapi.dtos.VotoCountDTO;
import br.com.robertocarneiro.pautaapi.dtos.VotoSaveDTO;
import br.com.robertocarneiro.pautaapi.entities.Associado;
import br.com.robertocarneiro.pautaapi.entities.Pauta;
import br.com.robertocarneiro.pautaapi.entities.Voto;

import java.util.Optional;

public interface VotoService {

    Optional<Voto> findFirstByAssociadoAndPauta(Associado associado, Pauta pauta);

    void save(VotoSaveDTO votoSaveDTO, Long pautaId, Long associadoId);

    VotoCountDTO voteCount(Pauta pauta);

    void validateIfCanVote(Associado associado, Pauta pauta);
}
