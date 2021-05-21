package br.com.robertocarneiro.pautaapi.enums;

import br.com.robertocarneiro.pautaapi.exceptions.InvalidVoteOptionException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum EnumBoolean {

    SIM("Sim"),
    NAO("Não");

    private String descricao;

    public static EnumBoolean findByDescricao(String descricao) {
        return Stream.of(values())
                .filter(e -> e.getDescricao().equals(descricao))
                .findFirst()
                .orElseThrow(InvalidVoteOptionException::new);
    }
}
