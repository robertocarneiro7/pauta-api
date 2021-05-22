package br.com.robertocarneiro.pautaapi.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude
public class PageDTO {

    private Long total;
    private Integer totalPaginas;
    private Integer pagina;
    private Integer tamanho;

}
