package br.com.robertocarneiro.pautaapi.services.impl;

import br.com.robertocarneiro.pautaapi.dtos.VotoSaveDTO;
import br.com.robertocarneiro.pautaapi.dtos.view.BotaoDTO;
import br.com.robertocarneiro.pautaapi.dtos.view.CampoDTO;
import br.com.robertocarneiro.pautaapi.dtos.view.OpcaoDTO;
import br.com.robertocarneiro.pautaapi.dtos.view.TelaFormularioDTO;
import br.com.robertocarneiro.pautaapi.entities.Associado;
import br.com.robertocarneiro.pautaapi.entities.Pauta;
import br.com.robertocarneiro.pautaapi.enums.EnumBoolean;
import br.com.robertocarneiro.pautaapi.enums.TipoCampo;
import br.com.robertocarneiro.pautaapi.enums.TipoTela;
import br.com.robertocarneiro.pautaapi.services.AssociadoService;
import br.com.robertocarneiro.pautaapi.services.PautaService;
import br.com.robertocarneiro.pautaapi.services.VotoService;
import br.com.robertocarneiro.pautaapi.services.VotoViewService;
import br.com.robertocarneiro.pautaapi.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class VotoViewServiceImpl implements VotoViewService {

    private final VotoService votoService;
    private final PautaService pautaService;
    private final AssociadoService associadoService;

    @Value("${server.url}")
    private String serverUrl;

    @Value("${controller.visualizar.path}")
    private String visualizarPath;

    @Value("${controller.voto.path}")
    private String votoPath;

    @Value("${controller.pauta-view.path}")
    private String pautaViewPath;

    @Value("${controller.pauta.path}")
    private String pautaPath;

    @Value("${header.associado-id.key}")
    private String headerAssociadoIdKey;

    @Override
    public TelaFormularioDTO viewVote(Long pautaId, Long associadoId) {
        Associado associado = associadoService.findById(associadoId);
        Pauta pauta = pautaService.findById(pautaId);
        votoService.validateIfCanVote(associado, pauta);

        List<CampoDTO> itens = new ArrayList<>();
        itens.add(CampoDTO
                .builder()
                .tipo(TipoCampo.TEXTO)
                .id(MessageUtil.get("label.field.pauta-name.id"))
                .titulo(MessageUtil.get("label.field.pauta-name.title"))
                .valor(pauta.getNome())
                .build());
        itens.add(CampoDTO
                .builder()
                .tipo(TipoCampo.INPUT_SELECT)
                .id(MessageUtil.get("label.field.vote-answer.id"))
                .titulo(MessageUtil.get("label.field.vote-answer.title"))
                .opcoes(optionsYesOrNo())
                .build());

        return TelaFormularioDTO
                .builder()
                .tipo(TipoTela.FORMULARIO)
                .titulo(MessageUtil.get("label.pauta-view.vote.title"))
                .itens(itens)
                .botaoOk(BotaoDTO
                        .builder()
                        .texto(MessageUtil.get("label.button.save"))
                        .url(serverUrl + votoPath + pautaPath + "/" + pautaId)
                        .metodo(HttpMethod.POST)
                        .body(VotoSaveDTO.builder().resposta("").build())
                        .headers(Map.of(headerAssociadoIdKey, MessageUtil.get("header.associado-id.desc-value")))
                        .build())
                .botaoCancelar(BotaoDTO
                        .builder()
                        .texto(MessageUtil.get("label.button.back"))
                        .url(serverUrl + pautaViewPath + visualizarPath + pautaPath + "/" + pautaId)
                        .metodo(HttpMethod.GET)
                        .build())
                .build();
    }

    private List<OpcaoDTO> optionsYesOrNo() {
        String selectLabel = MessageUtil.get("label.field.select.title");
        List<OpcaoDTO> options = new ArrayList<>();
        options.add(OpcaoDTO.builder().tipo(TipoCampo.TEXTO).texto(selectLabel).build());
        Stream
                .of(EnumBoolean.values())
                .map(EnumBoolean::getDescricao)
                .map(desc -> OpcaoDTO.builder().tipo(TipoCampo.TEXTO).texto(desc).valor(desc).build())
                .forEach(options::add);
        return options;
    }
}
