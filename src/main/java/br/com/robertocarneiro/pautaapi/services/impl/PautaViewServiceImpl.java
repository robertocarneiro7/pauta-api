package br.com.robertocarneiro.pautaapi.services.impl;

import br.com.robertocarneiro.pautaapi.dtos.PautaOpenVoteDTO;
import br.com.robertocarneiro.pautaapi.dtos.PautaSaveDTO;
import br.com.robertocarneiro.pautaapi.dtos.view.*;
import br.com.robertocarneiro.pautaapi.entities.Associado;
import br.com.robertocarneiro.pautaapi.entities.Pauta;
import br.com.robertocarneiro.pautaapi.enums.TipoCampo;
import br.com.robertocarneiro.pautaapi.enums.TipoTela;
import br.com.robertocarneiro.pautaapi.exceptions.VoteAlreadyOpenException;
import br.com.robertocarneiro.pautaapi.services.AssociadoService;
import br.com.robertocarneiro.pautaapi.services.PautaService;
import br.com.robertocarneiro.pautaapi.services.PautaViewService;
import br.com.robertocarneiro.pautaapi.services.VotoService;
import br.com.robertocarneiro.pautaapi.utils.MessageUtil;
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

    @Value("${header.associado-id.key}")
    private String headerAssociadoIdKey;

    private static final String LABEL_FIELD_NAME_ID = "label.field.name.id";

    private static final String LABEL_FIELD_NAME_TITLE = "label.field.name.title";

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
                        .headers(getHeadersAssociadoId())
                        .build())
                .collect(Collectors.toList());
        return TelaSelecaoDTO
                .builder()
                .tipo(TipoTela.SELECAO)
                .titulo(MessageUtil.get("label.pauta-view.view-list.title"))
                .itens(itens)
                .botaoOk(BotaoDTO
                        .builder()
                        .texto(MessageUtil.get("label.pauta-view.create.title"))
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
                .id(MessageUtil.get(LABEL_FIELD_NAME_ID))
                .titulo(MessageUtil.get(LABEL_FIELD_NAME_TITLE))
                .valor(pauta.getNome())
                .build());
        itens.add(CampoDTO
                .builder()
                .tipo(TipoCampo.TEXTO)
                .id(MessageUtil.get("label.field.description.id"))
                .titulo(MessageUtil.get("label.field.description.title"))
                .valor(pauta.getDescricao())
                .build());
        itens.add(CampoDTO
                .builder()
                .tipo(TipoCampo.TEXTO)
                .id(MessageUtil.get("label.field.date-open-vote.id"))
                .titulo(MessageUtil.get("label.field.date-open-vote.title"))
                .valor(pauta.getDataAberturaVotacao())
                .build());
        itens.add(CampoDTO
                .builder()
                .tipo(TipoCampo.TEXTO)
                .id(MessageUtil.get("label.field.date-close-vote.id"))
                .titulo(MessageUtil.get("label.field.date-close-vote.title"))
                .valor(pauta.getDataEncerramentoVotacao())
                .build());
        return TelaFormularioDTO
                .builder()
                .tipo(TipoTela.VISUALIZACAO)
                .titulo(MessageUtil.get("label.pauta-view.visualize.title"))
                .itens(itens)
                .botaoOk(buildBotaOkToViewVisualize(pauta, associado))
                .botaoCancelar(buildBotaoCancelar(serverUrl + pautaViewPath + listPath))
                .build();
    }

    private BotaoDTO buildBotaOkToViewVisualize(Pauta pauta, Associado associado) {
        if ((nonNull(pauta.getDataAberturaVotacao()) && LocalDateTime.now().isBefore(pauta.getDataAberturaVotacao()))
                || (nonNull(pauta.getDataEncerramentoVotacao()) && LocalDateTime.now().isAfter(pauta.getDataAberturaVotacao()))
                || isNull(associado)
                || votoService.findFirstByAssociadoAndPauta(associado, pauta).isPresent()) {
            return null;
        }
        String texto = MessageUtil.get("label.button.open-vote");
        String url = serverUrl + pautaViewPath + openVotePath + "/" + pauta.getPautaId();
        Map<String, Object> headers = null;
        if (nonNull(pauta.getDataAberturaVotacao())) {
            texto = MessageUtil.get("label.button.vote");
            url = serverUrl + votoViewPath + votePath + pautaPath + "/" + pauta.getPautaId();
            headers = getHeadersAssociadoId();
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
                .id(MessageUtil.get(LABEL_FIELD_NAME_ID))
                .titulo(MessageUtil.get(LABEL_FIELD_NAME_TITLE))
                .build());
        itens.add(CampoDTO
                .builder()
                .tipo(TipoCampo.INPUT_TEXTO_AREA)
                .id(MessageUtil.get("label.field.description.id"))
                .titulo(MessageUtil.get("label.field.description.title"))
                .build());
        return TelaFormularioDTO
                .builder()
                .tipo(TipoTela.FORMULARIO)
                .titulo(MessageUtil.get("label.pauta-view.create.title"))
                .itens(itens)
                .botaoOk(BotaoDTO
                        .builder()
                        .texto(MessageUtil.get("label.button.save"))
                        .url(serverUrl + pautaPath)
                        .metodo(HttpMethod.POST)
                        .body(PautaSaveDTO
                                .builder()
                                .nome("")
                                .descricao("")
                                .build())
                        .build())
                .botaoCancelar(buildBotaoCancelar(serverUrl + pautaViewPath + listPath))
                .build();
    }

    @Override
    public TelaFormularioDTO viewOpenVote(Long id) {
        Pauta pauta = pautaService.findById(id);
        if (nonNull(pauta.getDataAberturaVotacao()) || nonNull(pauta.getDataEncerramentoVotacao())) {
            throw new VoteAlreadyOpenException(Pauta.class, id);
        }
        List<CampoDTO> itens = new ArrayList<>();
        itens.add(CampoDTO
                .builder()
                .tipo(TipoCampo.TEXTO)
                .id(MessageUtil.get(LABEL_FIELD_NAME_ID))
                .titulo(MessageUtil.get(LABEL_FIELD_NAME_TITLE))
                .valor(pauta.getNome())
                .build());
        itens.add(CampoDTO
                .builder()
                .tipo(TipoCampo.INPUT_NUMERO)
                .id(MessageUtil.get("label.field.voting-duration.id"))
                .titulo(MessageUtil.get("label.field.voting-duration.title"))
                .build());
        return TelaFormularioDTO
                .builder()
                .tipo(TipoTela.FORMULARIO)
                .titulo(MessageUtil.get("label.pauta-view.open-vote.title"))
                .itens(itens)
                .botaoOk(BotaoDTO
                        .builder()
                        .texto(MessageUtil.get("label.button.save"))
                        .url(serverUrl + pautaPath + "/" + id + openVotePath)
                        .metodo(HttpMethod.PUT)
                        .body(PautaOpenVoteDTO.builder().duracaoVotacao(0).build())
                        .build())
                .botaoCancelar(buildBotaoCancelar(serverUrl + pautaViewPath + visualizePath + "/" + id))
                .build();
    }

    private BotaoDTO buildBotaoCancelar(String url) {
        return BotaoDTO
                .builder()
                .texto(MessageUtil.get("label.button.back"))
                .url(url)
                .metodo(HttpMethod.GET)
                .build();
    }

    private Map<String, Object> getHeadersAssociadoId() {
        return Map.of(headerAssociadoIdKey, MessageUtil.get("header.associado-id.desc-value"));
    }

}
