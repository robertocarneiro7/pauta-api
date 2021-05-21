package br.com.robertocarneiro.pautaapi.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotoCountDTO {

    private long countOptionYes;
    private long countOptionNo;
    private String mostVoted;

}
