package br.com.robertocarneiro.pautaapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PautaSaveDTO {

    private String nome;
    private String descricao;
}
