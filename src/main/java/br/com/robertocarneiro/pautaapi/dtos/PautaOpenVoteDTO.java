package br.com.robertocarneiro.pautaapi.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude
public class PautaOpenVoteDTO {

    @NotNull
    private Long pautaId;
    @Min(value = 1)
    private Long duracaoVotacao;

}
