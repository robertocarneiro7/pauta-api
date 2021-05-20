package br.com.robertocarneiro.pautaapi.dtos;

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
public class PautaViewListDTO {

    private TipoTela tipo;
    private String titulo;
    private List<PautaViewListItemDTO> itens;
}
