package br.com.robertocarneiro.pautaapi.dtos.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SelecaoItemDTO {

    private String texto;
    private String url;
    private Object body;
}
