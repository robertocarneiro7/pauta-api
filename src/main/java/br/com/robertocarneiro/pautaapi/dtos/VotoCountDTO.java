package br.com.robertocarneiro.pautaapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotoCountDTO {

    private long countOptionYes;
    private long countOptionNo;
    private String mostVoted;

}
