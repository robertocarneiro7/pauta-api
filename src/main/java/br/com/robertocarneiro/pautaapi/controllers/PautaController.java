package br.com.robertocarneiro.pautaapi.controllers;

import br.com.robertocarneiro.pautaapi.dtos.PautaOpenVoteDTO;
import br.com.robertocarneiro.pautaapi.dtos.PautaSaveDTO;
import br.com.robertocarneiro.pautaapi.services.PautaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("${controller.pauta.path}")
@RequiredArgsConstructor
public class PautaController {

    private final PautaService pautaService;

    @PostMapping
    public void save(@RequestBody @Valid PautaSaveDTO pautaSaveDTO) {
        pautaService.save(pautaSaveDTO);
    }

    @PutMapping("/{id}${controller.open-vote.path}")
    public void openVote(@PathVariable Long id,
                         @RequestBody @Valid PautaOpenVoteDTO pautaOpenVoteDTO) {
        pautaService.openVote(id, pautaOpenVoteDTO);
    }
}
