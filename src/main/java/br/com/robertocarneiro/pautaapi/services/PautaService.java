package br.com.robertocarneiro.pautaapi.services;

import br.com.robertocarneiro.pautaapi.dtos.PautaOpenVoteDTO;
import br.com.robertocarneiro.pautaapi.dtos.PautaSaveDTO;
import br.com.robertocarneiro.pautaapi.entities.Pauta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PautaService {

    Page<Pauta> findAll(Pageable pageable);

    Pauta findById(Long id);

    void save(PautaSaveDTO pautaSaveDTO);

    void openVote(PautaOpenVoteDTO pautaOpenVoteDTO);

    Pauta validateIfCanOpenVote(Long id);
}
