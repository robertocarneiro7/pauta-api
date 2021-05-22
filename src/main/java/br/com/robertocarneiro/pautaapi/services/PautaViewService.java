package br.com.robertocarneiro.pautaapi.services;

import br.com.robertocarneiro.pautaapi.dtos.AssociadoPageDTO;
import br.com.robertocarneiro.pautaapi.dtos.view.TelaFormularioDTO;
import br.com.robertocarneiro.pautaapi.dtos.view.TelaSelecaoDTO;

public interface PautaViewService {

    TelaSelecaoDTO viewList(AssociadoPageDTO dto);

    TelaFormularioDTO viewVisualize(Long pautaId, Long associadoId);

    TelaFormularioDTO viewCreate(Long associadoId);

    TelaFormularioDTO viewOpenVote(Long pautaId, Long associadoId);
}
