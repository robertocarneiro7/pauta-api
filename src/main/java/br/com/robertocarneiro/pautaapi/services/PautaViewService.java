package br.com.robertocarneiro.pautaapi.services;

import br.com.robertocarneiro.pautaapi.dtos.view.TelaFormularioDTO;
import br.com.robertocarneiro.pautaapi.dtos.view.TelaSelecaoDTO;

public interface PautaViewService {

    TelaSelecaoDTO viewList();

    TelaFormularioDTO viewVisualize(Long id, Long associadoId);

    TelaFormularioDTO viewCreate();

    TelaFormularioDTO viewOpenVote(Long id);
}
