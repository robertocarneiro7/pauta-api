package br.com.robertocarneiro.pautaapi.services.impl;

import br.com.robertocarneiro.pautaapi.dtos.AssociadoSaveDTO;
import br.com.robertocarneiro.pautaapi.entities.Associado;
import br.com.robertocarneiro.pautaapi.exceptions.EntityNotFoundException;
import br.com.robertocarneiro.pautaapi.mappers.AssociadoSaveDTOMapper;
import br.com.robertocarneiro.pautaapi.repositories.AssociadoRepository;
import br.com.robertocarneiro.pautaapi.services.AssociadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssociadoServiceImpl implements AssociadoService {

    private final AssociadoRepository repository;
    private final AssociadoSaveDTOMapper associadoSaveDTOMapper;

    @Override
    public Associado findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Associado.class, id));
    }

    @Override
    public void save(AssociadoSaveDTO associadoSaveDTO) {
        repository.save(associadoSaveDTOMapper.dtoToEntity(associadoSaveDTO));
    }
}
