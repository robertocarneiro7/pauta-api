package br.com.robertocarneiro.pautaapi.services.impl;

import br.com.robertocarneiro.pautaapi.dtos.*;
import br.com.robertocarneiro.pautaapi.dtos.view.*;
import br.com.robertocarneiro.pautaapi.entities.Associado;
import br.com.robertocarneiro.pautaapi.entities.Pauta;
import br.com.robertocarneiro.pautaapi.enums.EnumBoolean;
import br.com.robertocarneiro.pautaapi.enums.TipoCampo;
import br.com.robertocarneiro.pautaapi.enums.TipoTela;
import br.com.robertocarneiro.pautaapi.services.AssociadoService;
import br.com.robertocarneiro.pautaapi.services.PautaService;
import br.com.robertocarneiro.pautaapi.services.PautaViewService;
import br.com.robertocarneiro.pautaapi.services.VotoService;
import br.com.robertocarneiro.pautaapi.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @Value("${controller.listar.path}")
    private String listarPath;

    @Value("${controller.criar.path}")
    private String criarPath;

    @Value("${controller.visualizar.path}")
    private String visualizarPath;

    @Value("${controller.abrir-votacao.path}")
    private String abrirVotacaoPath;

    @Value("${controller.votar.path}")
    private String votarPath;

    @Value("${controller.pauta-view.path}")
    private String pautaViewPath;

    @Value("${controller.pauta.path}")
    private String pautaPath;

    @Value("${controller.voto-view.path}")
    private String votoViewPath;

    private static final String LABEL_FIELD_NAME_ID = "label.field.name.id";

    private static final String LABEL_FIELD_NAME_TITLE = "label.field.name.title";

    @Override
    public TelaSelecaoDTO viewList(AssociadoPageDTO dto) {
        Long associadoId = dto.getAssociadoId();
        if (nonNull(associadoId)) {
            associadoService.findById(associadoId);
        }
        Page<Pauta> pautasPage = pautaService.findAll(PageRequest.of(dto.getPagina(), dto.getTamanho()));
        List<SelecaoItemDTO> itens = pautasPage
                .stream()
                .map(pauta -> SelecaoItemDTO
                        .builder()
                        .texto(pauta.getNome())
                        .url(serverUrl + pautaViewPath + visualizarPath)
                        .body(PautaVisualizeDTO.builder().pautaId(pauta.getPautaId()).associadoId(associadoId).build())
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
                        .url(serverUrl + pautaViewPath + criarPath)
                        .body(AssociadoDTO.builder().associadoId(associadoId).build())
                        .build())
                .paginacao(PageDTO
                        .builder()
                        .total(pautasPage.getTotalElements())
                        .totalPaginas(pautasPage.getTotalPages())
                        .pagina(pautasPage.getPageable().getPageNumber())
                        .tamanho(pautasPage.getPageable().getPageSize())
                        .build())
                .build();
    }

    @Override
    public TelaFormularioDTO viewVisualize(Long pautaId, Long associadoId) {
        Pauta pauta = pautaService.findById(pautaId);
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

        VotoCountDTO votoCountDTO = votoService.voteCount(pauta);
        itens.add(CampoDTO
                .builder()
                .tipo(TipoCampo.TEXTO)
                .titulo(MessageUtil.get("label.field.total-vote-by-option.title", EnumBoolean.SIM.getDescricao()))
                .valor(votoCountDTO.getCountOptionYes())
                .build());
        itens.add(CampoDTO
                .builder()
                .tipo(TipoCampo.TEXTO)
                .titulo(MessageUtil.get("label.field.total-vote-by-option.title", EnumBoolean.NAO.getDescricao()))
                .valor(votoCountDTO.getCountOptionNo())
                .build());
        itens.add(CampoDTO
                .builder()
                .tipo(TipoCampo.TEXTO)
                .titulo(MessageUtil.get("label.field.most-voted-option.title"))
                .valor(votoCountDTO.getMostVoted())
                .build());
        return TelaFormularioDTO
                .builder()
                .tipo(TipoTela.VISUALIZACAO)
                .titulo(MessageUtil.get("label.pauta-view.visualize.title"))
                .itens(itens)
                .botaoOk(buildBotaOkToViewVisualize(pauta, associado))
                .botaoCancelar(
                        buildBotaoCancelar(serverUrl + pautaViewPath + listarPath,
                                AssociadoDTO.builder().associadoId(associadoId).build()))
                .build();
    }

    private BotaoDTO buildBotaOkToViewVisualize(Pauta pauta, Associado associado) {
        LocalDateTime now = LocalDateTime.now();
        if ((nonNull(pauta.getDataAberturaVotacao()) && now.isBefore(pauta.getDataAberturaVotacao()))
                || (nonNull(pauta.getDataEncerramentoVotacao()) && now.isAfter(pauta.getDataEncerramentoVotacao()))
                || isNull(associado)
                || votoService.findFirstByAssociadoAndPauta(associado, pauta).isPresent()) {
            return null;
        }
        String texto = MessageUtil.get("label.button.open-vote");
        String url = serverUrl + pautaViewPath + abrirVotacaoPath;
        Object body = PautaViewOpenVoteDTO.builder().pautaId(pauta.getPautaId()).associadoId(associado.getAssociadoId()).build();
        if (nonNull(pauta.getDataAberturaVotacao())) {
            texto = MessageUtil.get("label.button.vote");
            url = serverUrl + votoViewPath + votarPath;
            body = VotoViewDTO.builder().pautaId(pauta.getPautaId()).associadoId(associado.getAssociadoId()).build();
        }
        return BotaoDTO
                .builder()
                .texto(texto)
                .url(url)
                .body(body)
                .build();
    }

    @Override
    public TelaFormularioDTO viewCreate(Long associadoId) {
        if (nonNull(associadoId)) {
            associadoService.findById(associadoId);
        }
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
                        .body(PautaSaveDTO.builder().build())
                        .build())
                .botaoCancelar(buildBotaoCancelar(serverUrl + pautaViewPath + listarPath,
                        AssociadoDTO.builder().associadoId(associadoId).build()))
                .build();
    }

    @Override
    public TelaFormularioDTO viewOpenVote(Long pautaId, Long associadoId) {
        if (nonNull(associadoId)) {
            associadoService.findById(associadoId);
        }
        Pauta pauta = pautaService.validateIfCanOpenVote(pautaId);
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
                        .url(serverUrl + pautaPath + abrirVotacaoPath)
                        .body(PautaOpenVoteDTO.builder().pautaId(pautaId).build())
                        .build())
                .botaoCancelar(
                        buildBotaoCancelar(serverUrl + pautaViewPath + visualizarPath,
                                PautaVisualizeDTO.builder().pautaId(pautaId).associadoId(associadoId).build()))
                .build();
    }

    private BotaoDTO buildBotaoCancelar(String url, Object body) {
        return BotaoDTO
                .builder()
                .texto(MessageUtil.get("label.button.back"))
                .url(url)
                .body(body)
                .build();
    }

}
