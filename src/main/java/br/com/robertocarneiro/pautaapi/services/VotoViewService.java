package br.com.robertocarneiro.pautaapi.services;

import br.com.robertocarneiro.pautaapi.dtos.view.TelaFormularioDTO;

public interface VotoViewService {

    TelaFormularioDTO viewVote(Long pautaId, Long associadoId);
}
