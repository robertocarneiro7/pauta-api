package br.com.robertocarneiro.pautaapi.services.impl;

import br.com.robertocarneiro.pautaapi.entities.Associado;
import br.com.robertocarneiro.pautaapi.entities.Pauta;
import br.com.robertocarneiro.pautaapi.entities.Voto;
import br.com.robertocarneiro.pautaapi.repositories.VotoRepository;
import br.com.robertocarneiro.pautaapi.services.VotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VotoServiceImpl implements VotoService {

    private final VotoRepository repository;

    @Override
    public Optional<Voto> findFirstByAssociadoAndPauta(Associado associado, Pauta pauta) {
        return repository.findFirstByAssociadoAndPauta(associado, pauta);
    }
}
