package br.com.robertocarneiro.pautaapi.services.impl;

import br.com.robertocarneiro.pautaapi.dtos.PautaSaveDTO;
import br.com.robertocarneiro.pautaapi.dtos.view.*;
import br.com.robertocarneiro.pautaapi.enums.TipoCampo;
import br.com.robertocarneiro.pautaapi.enums.TipoTela;
import br.com.robertocarneiro.pautaapi.services.PautaService;
import br.com.robertocarneiro.pautaapi.services.PautaViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PautaViewServiceImpl implements PautaViewService {

    private final PautaService pautaService;

    @Value("${server.url}")
    private String serverUrl;

    @Value("${controller.list.path}")
    private String listPath;

    @Value("${controller.create.path}")
    private String createPath;

    @Value("${controller.pauta-view.path}")
    private String pautaViewPath;

    @Value("${controller.pauta.path}")
    private String pautaPath;

    @Value("${label.pauta-view.view-list.title}")
    private String labelPautaViewListTitle;

    @Value("${label.pauta-view.create.title}")
    private String labelPautaViewCreateTitle;

    @Value("${label.pauta-view.create.field.name}")
    private String labelPautaViewCreateFieldName;

    @Value("${label.pauta-view.create.field.description}")
    private String labelPautaViewCreateFieldDescription;

    @Value("${label.pauta-view.create.button.save}")
    private String labelPautaViewCreateButtonSave;

    @Value("${label.pauta-view.create.button.cancel}")
    private String labelPautaViewCreateButtonCancel;

    @Override
    public TelaSelecaoDTO viewList() {
        List<SelecaoItemDTO> itens = pautaService
                .findAll()
                .stream()
                .map(pauta -> SelecaoItemDTO
                        .builder()
                        .texto(pauta.getNome())
                        .url(serverUrl + pautaViewPath + listPath + "/" + pauta.getPautaId())
                        .metodo(HttpMethod.GET)
                        .build())
                .collect(Collectors.toList());
        return TelaSelecaoDTO
                .builder()
                .tipo(TipoTela.SELECAO)
                .titulo(labelPautaViewListTitle)
                .itens(itens)
                .botaoOk(BotaoDTO
                        .builder()
                        .texto(labelPautaViewCreateTitle)
                        .url(serverUrl + pautaViewPath + createPath)
                        .metodo(HttpMethod.GET)
                        .build())
                .build();
    }

    @Override
    public TelaFormularioDTO viewCreate() {
        List<CampoDTO> itens = new ArrayList<>();
        itens.add(CampoDTO
                .builder()
                .tipo(TipoCampo.INPUT_TEXTO)
                .id("nome")
                .titulo(labelPautaViewCreateFieldName)
                .build());
        itens.add(CampoDTO
                .builder()
                .tipo(TipoCampo.INPUT_TEXTO_AREA)
                .id("descricao")
                .titulo(labelPautaViewCreateFieldDescription)
                .build());
        return TelaFormularioDTO
                .builder()
                .tipo(TipoTela.FORMULARIO)
                .titulo(labelPautaViewCreateTitle)
                .itens(itens)
                .botaoOk(BotaoDTO
                        .builder()
                        .texto(labelPautaViewCreateButtonSave)
                        .url(serverUrl + pautaPath)
                        .metodo(HttpMethod.POST)
                        .body(PautaSaveDTO
                                .builder()
                                .nome("")
                                .descricao("")
                                .build())
                        .build())
                .botaoCancelar(BotaoDTO
                        .builder()
                        .texto(labelPautaViewCreateButtonCancel)
                        .url(serverUrl + pautaViewPath + listPath)
                        .metodo(HttpMethod.GET)
                        .build())
                .build();
    }

}
