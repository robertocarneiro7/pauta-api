package br.com.robertocarneiro.pautaapi.services.impl;

import br.com.robertocarneiro.pautaapi.entities.Associado;
import br.com.robertocarneiro.pautaapi.exceptions.NotFoundException;
import br.com.robertocarneiro.pautaapi.repositories.AssociadoRepository;
import br.com.robertocarneiro.pautaapi.services.AssociadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssociadoServiceImpl implements AssociadoService {

    private final AssociadoRepository repository;

    @Override
    public Associado findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(Associado.class, id));
    }
}
