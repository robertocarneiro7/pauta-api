package br.com.robertocarneiro.pautaapi.dtos;

import br.com.robertocarneiro.pautaapi.enums.VoteSituationStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserByCpfDTO {

    private VoteSituationStatus status;

}
