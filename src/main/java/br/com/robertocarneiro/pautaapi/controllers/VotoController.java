package br.com.robertocarneiro.pautaapi.controllers;

import br.com.robertocarneiro.pautaapi.dtos.VotoSaveDTO;
import br.com.robertocarneiro.pautaapi.services.VotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("${controller.voto.path}")
@RequiredArgsConstructor
@Validated
public class VotoController {

    private final VotoService votoService;

    @PostMapping("${controller.pauta.path}/{pautaId}")
    public void save(
            @RequestBody @Valid VotoSaveDTO votoSaveDTO,
            @PathVariable Long pautaId,
            @RequestHeader(name = "${header.associado-id.key}", required = false) @NotNull Long associadoId) {
        votoService.save(votoSaveDTO, pautaId, associadoId);
    }

}
