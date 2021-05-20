package br.com.robertocarneiro.pautaapi.repositories;

import br.com.robertocarneiro.pautaapi.entities.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PautaRepository extends JpaRepository<Pauta, Long> {
}
