package br.com.robertocarneiro.pautaapi.services;

import br.com.robertocarneiro.pautaapi.entities.Associado;
import br.com.robertocarneiro.pautaapi.entities.Pauta;
import br.com.robertocarneiro.pautaapi.entities.Voto;

import java.util.Optional;

public interface VotoService {

    Optional<Voto> findFirstByAssociadoAndPauta(Associado associado, Pauta pauta);
}