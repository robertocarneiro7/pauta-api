package br.com.robertocarneiro.pautaapi.dtos.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SelecaoItemDTO {

    private String texto;
    private String url;
    private HttpMethod metodo;
}
