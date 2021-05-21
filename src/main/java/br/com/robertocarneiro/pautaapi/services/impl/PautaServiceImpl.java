package br.com.robertocarneiro.pautaapi.services.impl;

import br.com.robertocarneiro.pautaapi.dtos.PautaSaveDTO;
import br.com.robertocarneiro.pautaapi.entities.Pauta;
import br.com.robertocarneiro.pautaapi.exceptions.FieldCantBeRepeatException;
import br.com.robertocarneiro.pautaapi.exceptions.EntityNotFoundException;
import br.com.robertocarneiro.pautaapi.mappers.PautaSaveDTOMapper;
import br.com.robertocarneiro.pautaapi.repositories.PautaRepository;
import br.com.robertocarneiro.pautaapi.services.PautaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PautaServiceImpl implements PautaService {

    private final PautaRepository repository;
    private final PautaSaveDTOMapper pautaSaveDTOMapper;

    @Override
    public List<Pauta> findAll() {
        return repository.findAll();
    }

    @Override
    public Pauta findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Pauta.class, id));
    }

    @Override
    public void save(PautaSaveDTO pautaSaveDTO) {
        Pauta pauta = pautaSaveDTOMapper.dtoToEntity(pautaSaveDTO);
        if (repository.findFirstByNome(pauta.getNome()).isPresent()) {
            throw new FieldCantBeRepeatException(Pauta.class, "nome", pauta.getNome());
        }
        repository.save(pauta);
    }
}
