package br.com.robertocarneiro.pautaapi.services;

import br.com.robertocarneiro.pautaapi.dtos.view.TelaFormularioDTO;
import br.com.robertocarneiro.pautaapi.dtos.view.TelaSelecaoDTO;

public interface PautaViewService {

    TelaSelecaoDTO viewList();

    TelaFormularioDTO viewCreate();
}
