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
public class VotoViewDTO {

    @NotNull
    private Long pautaId;
    @NotNull
    private Long associadoId;

}
