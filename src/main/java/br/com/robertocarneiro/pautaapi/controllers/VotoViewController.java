package br.com.robertocarneiro.pautaapi.controllers;

import br.com.robertocarneiro.pautaapi.dtos.VotoViewDTO;
import br.com.robertocarneiro.pautaapi.dtos.view.TelaFormularioDTO;
import br.com.robertocarneiro.pautaapi.services.VotoViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("${controller.voto-view.path}")
@RequiredArgsConstructor
public class VotoViewController {

    private final VotoViewService votoViewService;

    @PostMapping("${controller.votar.path}")
    public TelaFormularioDTO viewVote(@RequestBody @Valid VotoViewDTO dto) {
        return votoViewService.viewVote(dto.getPautaId(), dto.getAssociadoId());
    }

}
