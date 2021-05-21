package br.com.robertocarneiro.pautaapi.dtos;

import lombok.*;

import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PautaOpenVoteDTO {

    @Min(value = 1)
    private Long duracaoVotacao;

}
