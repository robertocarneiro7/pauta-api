package br.com.robertocarneiro.pautaapi.services.impl;

import br.com.robertocarneiro.pautaapi.dtos.PautaViewListDTO;
import br.com.robertocarneiro.pautaapi.dtos.PautaViewListItemDTO;
import br.com.robertocarneiro.pautaapi.enums.TipoTela;
import br.com.robertocarneiro.pautaapi.services.PautaService;
import br.com.robertocarneiro.pautaapi.services.PautaViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PautaViewServiceImpl implements PautaViewService {

    private final PautaService pautaService;

    @Value("${server.url}")
    private String serverUrl;

    @Value("${controller.pauta-view.path}")
    private String pautaViewPath;

    @Value("${label.pauta-view.title}")
    private String labelPautaViewTitle;

    @Override
    public PautaViewListDTO viewList() {
        List<PautaViewListItemDTO> itens = pautaService
                .findAll()
                .stream()
                .map(pauta -> PautaViewListItemDTO
                        .builder()
                        .texto(pauta.getNome())
                        .url(serverUrl + pautaViewPath + "/" + pauta.getPautaId())
                        .metodo(HttpMethod.GET)
                        .build())
                .collect(Collectors.toList());
        return PautaViewListDTO
                .builder()
                .tipo(TipoTela.SELECAO)
                .titulo(labelPautaViewTitle)
                .itens(itens)
                .build();
    }

}
