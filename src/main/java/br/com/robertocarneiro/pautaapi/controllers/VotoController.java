package br.com.robertocarneiro.pautaapi.controllers;

import br.com.robertocarneiro.pautaapi.dtos.VotoSaveDTO;
import br.com.robertocarneiro.pautaapi.services.VotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("${controller.voto.path}")
@RequiredArgsConstructor
public class VotoController {

    private final VotoService votoService;

    @PostMapping("${controller.pauta.path}/{pautaId}")
    public void save(
            @RequestBody @Valid VotoSaveDTO votoSaveDTO,
            @PathVariable Long pautaId,
            @RequestHeader(name = "${header.associado-id.key}") @Valid Long associadoId) {
        votoService.save(votoSaveDTO, pautaId, associadoId);
    }

}
