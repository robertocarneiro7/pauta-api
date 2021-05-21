package br.com.robertocarneiro.pautaapi.controllers;

import br.com.robertocarneiro.pautaapi.dtos.view.TelaFormularioDTO;
import br.com.robertocarneiro.pautaapi.dtos.view.TelaSelecaoDTO;
import br.com.robertocarneiro.pautaapi.services.PautaViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${controller.pauta-view.path}")
@RequiredArgsConstructor
public class PautaViewController {

    private final PautaViewService pautaViewService;

    @GetMapping("${controller.list.path}")
    public TelaSelecaoDTO viewList() {
        return pautaViewService.viewList();
    }

    @GetMapping("/{id}${controller.visualize.path}")
    public TelaFormularioDTO viewVisualize(
            @PathVariable Long id,
            @RequestHeader(name = "${header.associado-id.key}", required = false) Long associadoId) {
        return pautaViewService.viewVisualize(id, associadoId);
    }

    @GetMapping("${controller.create.path}")
    public TelaFormularioDTO viewCreate() {
        return pautaViewService.viewCreate();
    }

    @GetMapping("/{id}${controller.open-vote.path}")
    public TelaFormularioDTO viewOpenVote(@PathVariable Long id) {
        return pautaViewService.viewOpenVote(id);
    }

}
