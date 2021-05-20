package br.com.robertocarneiro.pautaapi.repositories;

import br.com.robertocarneiro.pautaapi.entities.Associado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssociadoRepository extends JpaRepository<Associado, Long> {
}
