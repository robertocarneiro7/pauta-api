package br.com.robertocarneiro.pautaapi.services.impl;

import br.com.robertocarneiro.pautaapi.dtos.PautaSaveDTO;
import br.com.robertocarneiro.pautaapi.dtos.view.*;
import br.com.robertocarneiro.pautaapi.entities.Associado;
import br.com.robertocarneiro.pautaapi.entities.Pauta;
import br.com.robertocarneiro.pautaapi.enums.TipoCampo;
import br.com.robertocarneiro.pautaapi.enums.TipoTela;
import br.com.robertocarneiro.pautaapi.services.AssociadoService;
import br.com.robertocarneiro.pautaapi.services.PautaService;
import br.com.robertocarneiro.pautaapi.services.PautaViewService;
import br.com.robertocarneiro.pautaapi.services.VotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class PautaViewServiceImpl implements PautaViewService {

    private final PautaService pautaService;
    private final AssociadoService associadoService;
    private final VotoService votoService;

    @Value("${server.url}")
    private String serverUrl;

    @Value("${controller.list.path}")
    private String listPath;

    @Value("${controller.create.path}")
    private String createPath;

    @Value("${controller.visualize.path}")
    private String visualizePath;

    @Value("${controller.open-vote.path}")
    private String openVotePath;

    @Value("${controller.vote.path}")
    private String votePath;

    @Value("${controller.pauta-view.path}")
    private String pautaViewPath;

    @Value("${controller.pauta.path}")
    private String pautaPath;

    @Value("${controller.voto-view.path}")
    private String votoViewPath;

    @Value("${label.pauta-view.view-list.title}")
    private String labelPautaViewListTitle;

    @Value("${label.pauta-view.visualize.title}")
    private String labelPautaViewVisualizeTitle;

    @Value("${label.pauta-view.create.title}")
    private String labelPautaViewCreateTitle;

    @Value("${label.field.name}")
    private String labelPautaViewFieldName;

    @Value("${label.field.description}")
    private String labelPautaViewFieldDescription;

    @Value("${label.button.save}")
    private String labelPautaViewButtonSave;

    @Value("${label.button.vote}")
    private String labelPautaViewButtonVote;

    @Value("${label.button.open-vote}")
    private String labelPautaViewButtonOpenVote;

    @Value("${label.button.back}")
    private String labelPautaViewButtonBack;

    @Value("${header.associado-id.key}")
    private String headerAssociadoIdKey;

    @Value("${header.associado-id.desc-value}")
    private String headerAssociadoIdDescValue;

    @Override
    public TelaSelecaoDTO viewList() {
        List<SelecaoItemDTO> itens = pautaService
                .findAll()
                .stream()
                .map(pauta -> SelecaoItemDTO
                        .builder()
                        .texto(pauta.getNome())
                        .url(serverUrl + pautaViewPath + visualizePath + "/" + pauta.getPautaId())
                        .metodo(HttpMethod.GET)
                        .headers(Map.of(headerAssociadoIdKey, headerAssociadoIdDescValue))
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
    public TelaFormularioDTO viewVisualize(Long id, Long associadoId) {
        Pauta pauta = pautaService.findById(id);
        Associado associado = null;
        if (nonNull(associadoId)) {
            associado = associadoService.findById(associadoId);
        }
        List<CampoDTO> itens = new ArrayList<>();
        itens.add(CampoDTO
                .builder()
                .tipo(TipoCampo.TEXTO)
                .id("nome")
                .titulo(labelPautaViewFieldName)
                .valor(pauta.getNome())
                .build());
        itens.add(CampoDTO
                .builder()
                .tipo(TipoCampo.TEXTO)
                .id("descricao")
                .titulo(labelPautaViewFieldDescription)
                .valor(pauta.getDescricao())
                .build());
        return TelaFormularioDTO
                .builder()
                .tipo(TipoTela.FORMULARIO)
                .titulo(labelPautaViewVisualizeTitle)
                .itens(itens)
                .botaoOk(buildBotaOkToViewVisualize(pauta, associado))
                .botaoCancelar(buildBotaoCancelar())
                .build();
    }

    private BotaoDTO buildBotaOkToViewVisualize(Pauta pauta, Associado associado) {
        if ((nonNull(pauta.getDataAberturaVotacao()) && LocalDateTime.now().isBefore(pauta.getDataAberturaVotacao()))
                || (nonNull(pauta.getDataEncerramentoVotacao()) && LocalDateTime.now().isAfter(pauta.getDataAberturaVotacao()))
                || isNull(associado)
                || votoService.findFirstByAssociadoAndPauta(associado, pauta).isPresent()) {
            return null;
        }
        String texto = labelPautaViewButtonOpenVote;
        String url = serverUrl + pautaViewPath + openVotePath + "/" + pauta.getPautaId();
        Map<String, Object> headers = null;
        if (nonNull(pauta.getDataAberturaVotacao())) {
            texto = labelPautaViewButtonVote;
            url = serverUrl + votoViewPath + votePath + pautaPath + "/" + pauta.getPautaId();
            headers = Map.of(headerAssociadoIdKey, headerAssociadoIdDescValue);
        }
        return BotaoDTO
                .builder()
                .texto(texto)
                .url(url)
                .metodo(HttpMethod.GET)
                .headers(headers)
                .build();
    }

    @Override
    public TelaFormularioDTO viewCreate() {
        List<CampoDTO> itens = new ArrayList<>();
        itens.add(CampoDTO
                .builder()
                .tipo(TipoCampo.INPUT_TEXTO)
                .id("nome")
                .titulo(labelPautaViewFieldName)
                .build());
        itens.add(CampoDTO
                .builder()
                .tipo(TipoCampo.INPUT_TEXTO_AREA)
                .id("descricao")
                .titulo(labelPautaViewFieldDescription)
                .build());
        return TelaFormularioDTO
                .builder()
                .tipo(TipoTela.FORMULARIO)
                .titulo(labelPautaViewCreateTitle)
                .itens(itens)
                .botaoOk(BotaoDTO
                        .builder()
                        .texto(labelPautaViewButtonSave)
                        .url(serverUrl + pautaPath)
                        .metodo(HttpMethod.POST)
                        .body(PautaSaveDTO
                                .builder()
                                .nome("")
                                .descricao("")
                                .build())
                        .build())
                .botaoCancelar(buildBotaoCancelar())
                .build();
    }

    private BotaoDTO buildBotaoCancelar() {
        return BotaoDTO
                .builder()
                .texto(labelPautaViewButtonBack)
                .url(serverUrl + pautaViewPath + listPath)
                .metodo(HttpMethod.GET)
                .build();
    }

}
