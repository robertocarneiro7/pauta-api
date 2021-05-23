package br.com.robertocarneiro.pautaapi.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude
public class AssociadoSaveDTO {

    @NotBlank
    private String cpf;

}
