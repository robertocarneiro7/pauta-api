package br.com.robertocarneiro.pautaapi.dtos.view;

import br.com.robertocarneiro.pautaapi.enums.TipoTela;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelaSelecaoDTO {

    private static final TipoTela tipo = TipoTela.SELECAO;
    private String titulo;
    private List<SelecaoItemDTO> itens;
    private BotaoDTO botaoOk;
}
