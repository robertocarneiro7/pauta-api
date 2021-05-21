package br.com.robertocarneiro.pautaapi.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotoSaveDTO {

    @NotBlank
    private String resposta;

}
