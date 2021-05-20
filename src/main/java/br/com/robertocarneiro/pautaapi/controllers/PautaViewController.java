package br.com.robertocarneiro.pautaapi.controllers;

import br.com.robertocarneiro.pautaapi.dtos.PautaViewListDTO;
import br.com.robertocarneiro.pautaapi.services.PautaViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "${controller.pauta-view.path}")
@RequiredArgsConstructor
public class PautaViewController {

    private final PautaViewService pautaViewService;

    @GetMapping
    public PautaViewListDTO viewList() {
        return pautaViewService.viewList();
    }
}
