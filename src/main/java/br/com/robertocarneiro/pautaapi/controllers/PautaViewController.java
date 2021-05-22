package br.com.robertocarneiro.pautaapi.controllers;

import br.com.robertocarneiro.pautaapi.dtos.AssociadoDTO;
import br.com.robertocarneiro.pautaapi.dtos.PautaViewOpenVoteDTO;
import br.com.robertocarneiro.pautaapi.dtos.PautaVisualizeDTO;
import br.com.robertocarneiro.pautaapi.dtos.view.TelaFormularioDTO;
import br.com.robertocarneiro.pautaapi.dtos.view.TelaSelecaoDTO;
import br.com.robertocarneiro.pautaapi.services.PautaViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("${controller.pauta-view.path}")
@RequiredArgsConstructor
public class PautaViewController {

    private final PautaViewService pautaViewService;

    @PostMapping("${controller.listar.path}")
    public TelaSelecaoDTO viewList(@RequestBody AssociadoDTO dto) {
        return pautaViewService.viewList(dto.getAssociadoId());
    }

    @PostMapping("${controller.visualizar.path}")
    public TelaFormularioDTO viewVisualize(@RequestBody @Valid PautaVisualizeDTO dto) {
        return pautaViewService.viewVisualize(dto.getPautaId(), dto.getAssociadoId());
    }

    @PostMapping("${controller.criar.path}")
    public TelaFormularioDTO viewCreate(@RequestBody AssociadoDTO dto) {
        return pautaViewService.viewCreate(dto.getAssociadoId());
    }

    @PostMapping("${controller.abrir-votacao.path}")
    public TelaFormularioDTO viewOpenVote(@RequestBody @Valid PautaViewOpenVoteDTO dto) {
        return pautaViewService.viewOpenVote(dto.getPautaId(), dto.getAssociadoId());
    }

}
