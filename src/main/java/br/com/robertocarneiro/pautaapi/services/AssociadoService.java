package br.com.robertocarneiro.pautaapi.services;

import br.com.robertocarneiro.pautaapi.entities.Associado;

public interface AssociadoService {

    Associado findById(Long id);
}
