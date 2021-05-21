package br.com.robertocarneiro.pautaapi.dtos.view;

import br.com.robertocarneiro.pautaapi.enums.TipoCampo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampoDTO {

    private TipoCampo tipo;
    private String texto;
    private String id;
    private String titulo;
    private Object valor;
}
