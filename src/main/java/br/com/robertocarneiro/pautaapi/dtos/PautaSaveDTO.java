package br.com.robertocarneiro.pautaapi.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PautaSaveDTO {

    @NotBlank
    private String nome;
    @NotBlank
    private String descricao;
}
