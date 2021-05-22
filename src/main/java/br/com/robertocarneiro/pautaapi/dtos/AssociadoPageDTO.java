package br.com.robertocarneiro.pautaapi.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude
public class AssociadoPageDTO {

    private Long associadoId;
    @NotNull
    private Integer pagina;
    @NotNull
    private Integer tamanho;

}
