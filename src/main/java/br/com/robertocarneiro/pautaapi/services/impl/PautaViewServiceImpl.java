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

    @Value("${label.pauta-view.open-vote.title}")
    private String labelPautaViewOpenVoteTitle;

    @Value("${label.field.name.id}")
    private String labelFieldNameId;

    @Value("${label.field.name.title}")
    private String labelFieldNameTitle;

    @Value("${label.field.description.id}")
    private String labelFieldDescriptionId;

    @Value("${label.field.description.title}")
    private String labelFieldDescriptionTitle;

    @Value("${label.field.date-open-vote.id}")
    private String labelFieldDateOpenVoteId;

    @Value("${label.field.date-open-vote.title}")
    private String labelFieldDateOpenVoteTitle;

    @Value("${label.field.date-close-vote.id}")
    private String labelFieldDateCloseVoteId;

    @Value("${label.field.date-close-vote.title}")
    private String labelFieldDateCloseVoteTitle;

    @Value("${label.field.voting-duration.id}")
    private String labelFieldVotingDurationId;

    @Value("${label.field.voting-duration.title}")
    private String labelFieldVotingDurationTitle;

    @Value("${label.button.save}")
    private String labelButtonSave;

    @Value("${label.button.vote}")
    private String labelButtonVote;

    @Value("${label.button.open-vote}")
    private String labelButtonOpenVote;

    @Value("${label.button.back}")
    private String labelButtonBack;

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
                .id(labelFieldNameId)
                .titulo(labelFieldNameTitle)
                .valor(pauta.getNome())
                .build());
        itens.add(CampoDTO
                .builder()
                .tipo(TipoCampo.TEXTO)
                .id(labelFieldDescriptionId)
                .titulo(labelFieldDescriptionTitle)
                .valor(pauta.getDescricao())
                .build());
        itens.add(CampoDTO
                .builder()
                .tipo(TipoCampo.TEXTO)
                .id(labelFieldDateOpenVoteId)
                .titulo(labelFieldDateOpenVoteTitle)
                .valor(pauta.getDataAberturaVotacao())
                .build());
        itens.add(CampoDTO
                .builder()
                .tipo(TipoCampo.TEXTO)
                .id(labelFieldDateCloseVoteId)
                .titulo(labelFieldDateCloseVoteTitle)
                .valor(pauta.getDataEncerramentoVotacao())
                .build());
        return TelaFormularioDTO
                .builder()
                .tipo(TipoTela.VISUALIZACAO)
                .titulo(labelPautaViewVisualizeTitle)
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
        String texto = labelButtonOpenVote;
        String url = serverUrl + pautaViewPath + openVotePath + "/" + pauta.getPautaId();
        Map<String, Object> headers = null;
        if (nonNull(pauta.getDataAberturaVotacao())) {
            texto = labelButtonVote;
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
                .id(labelFieldNameId)
                .titulo(labelFieldNameTitle)
                .build());
        itens.add(CampoDTO
                .builder()
                .tipo(TipoCampo.INPUT_TEXTO_AREA)
                .id(labelFieldDescriptionId)
                .titulo(labelFieldDescriptionTitle)
                .build());
        return TelaFormularioDTO
                .builder()
                .tipo(TipoTela.FORMULARIO)
                .titulo(labelPautaViewCreateTitle)
                .itens(itens)
                .botaoOk(BotaoDTO
                        .builder()
                        .texto(labelButtonSave)
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
                .id(labelFieldNameId)
                .titulo(labelFieldNameTitle)
                .valor(pauta.getNome())
                .build());
        itens.add(CampoDTO
                .builder()
                .tipo(TipoCampo.INPUT_NUMERO)
                .id(labelFieldVotingDurationId)
                .titulo(labelFieldVotingDurationTitle)
                .build());
        return TelaFormularioDTO
                .builder()
                .tipo(TipoTela.FORMULARIO)
                .titulo(labelPautaViewOpenVoteTitle)
                .itens(itens)
                .botaoOk(BotaoDTO
                        .builder()
                        .texto(labelButtonSave)
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
                .texto(labelButtonBack)
                .url(url)
                .metodo(HttpMethod.GET)
                .build();
    }

}
