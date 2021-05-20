package br.com.robertocarneiro.pautaapi.repositories;

import br.com.robertocarneiro.pautaapi.entities.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PautaRepository extends JpaRepository<Pauta, Long> {

    Optional<Pauta> findFirstByNome(String nome);
}
