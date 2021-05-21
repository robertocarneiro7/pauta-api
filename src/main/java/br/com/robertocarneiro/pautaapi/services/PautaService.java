package br.com.robertocarneiro.pautaapi.services;

import br.com.robertocarneiro.pautaapi.dtos.PautaOpenVoteDTO;
import br.com.robertocarneiro.pautaapi.dtos.PautaSaveDTO;
import br.com.robertocarneiro.pautaapi.entities.Pauta;

import java.util.List;

public interface PautaService {

    List<Pauta> findAll();

    Pauta findById(Long id);

    void save(PautaSaveDTO pautaSaveDTO);

    void openVote(Long id, PautaOpenVoteDTO pautaOpenVoteDTO);

    Pauta validateIfCanOpenVote(Long id);
}
