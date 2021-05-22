package br.com.robertocarneiro.pautaapi.controllers;

import br.com.robertocarneiro.pautaapi.dtos.view.TelaFormularioDTO;
import br.com.robertocarneiro.pautaapi.services.VotoViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("${controller.voto-view.path}")
@RequiredArgsConstructor
public class VotoViewController {

    private final VotoViewService votoViewService;

    @GetMapping("${controller.votar.path}${controller.pauta.path}/{pautaId}")
    public TelaFormularioDTO viewVote(
            @PathVariable Long pautaId,
            @RequestHeader(name = "Associado-Id") @Valid Long associadoId) {
        return votoViewService.viewVote(pautaId, associadoId);
    }

}
