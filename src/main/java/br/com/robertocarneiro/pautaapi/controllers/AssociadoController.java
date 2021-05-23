package br.com.robertocarneiro.pautaapi.controllers;

import br.com.robertocarneiro.pautaapi.dtos.AssociadoSaveDTO;
import br.com.robertocarneiro.pautaapi.services.AssociadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/associado")
@RequiredArgsConstructor
@Profile("performance-test")
public class AssociadoController {

    private final AssociadoService associadoService;

    @PostMapping
    public void save(@RequestBody @Valid AssociadoSaveDTO associadoSaveDTO) {
        associadoService.save(associadoSaveDTO);
    }
}
