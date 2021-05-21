package br.com.robertocarneiro.pautaapi.services.impl;

import br.com.robertocarneiro.pautaapi.dtos.PautaOpenVoteDTO;
import br.com.robertocarneiro.pautaapi.dtos.PautaSaveDTO;
import br.com.robertocarneiro.pautaapi.entities.Pauta;
import br.com.robertocarneiro.pautaapi.exceptions.EntityNotFoundException;
import br.com.robertocarneiro.pautaapi.exceptions.FieldCantBeRepeatException;
import br.com.robertocarneiro.pautaapi.exceptions.VoteAlreadyOpenException;
import br.com.robertocarneiro.pautaapi.mappers.PautaSaveDTOMapper;
import br.com.robertocarneiro.pautaapi.repositories.PautaRepository;
import br.com.robertocarneiro.pautaapi.services.PautaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class PautaServiceImpl implements PautaService {

    private final PautaRepository repository;
    private final PautaSaveDTOMapper pautaSaveDTOMapper;

    @Value("${value.default.voting-duration}")
    private Long defaultVotingDuration;

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

    @Override
    public void openVote(Long id, PautaOpenVoteDTO pautaOpenVoteDTO) {
        Pauta pauta = validateIfCanOpenVote(id);

        Long votingDuration = ofNullable(pautaOpenVoteDTO.getDuracaoVotacao()).orElse(defaultVotingDuration);
        pauta.setDataAberturaVotacao(LocalDateTime.now());
        pauta.setDataEncerramentoVotacao(pauta.getDataAberturaVotacao().plusMinutes(votingDuration));
        repository.save(pauta);
    }

    @Override
    public Pauta validateIfCanOpenVote(Long id) {
        Pauta pauta = findById(id);
        if (nonNull(pauta.getDataAberturaVotacao()) || nonNull(pauta.getDataEncerramentoVotacao())) {
            throw new VoteAlreadyOpenException(Pauta.class, id);
        }
        return pauta;
    }
}
