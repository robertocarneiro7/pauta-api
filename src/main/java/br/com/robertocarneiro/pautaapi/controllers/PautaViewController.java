package br.com.robertocarneiro.pautaapi.controllers;

import br.com.robertocarneiro.pautaapi.dtos.view.TelaFormularioDTO;
import br.com.robertocarneiro.pautaapi.dtos.view.TelaSelecaoDTO;
import br.com.robertocarneiro.pautaapi.services.PautaViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${controller.pauta-view.path}")
@RequiredArgsConstructor
public class PautaViewController {

    private final PautaViewService pautaViewService;

    @GetMapping("${controller.list.path}")
    public TelaSelecaoDTO viewList() {
        return pautaViewService.viewList();
    }

    @GetMapping("${controller.create.path}")
    public TelaFormularioDTO viewCreate() {
        return pautaViewService.viewCreate();
    }
}
