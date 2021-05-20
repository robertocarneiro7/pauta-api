package br.com.robertocarneiro.pautaapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumBoolean {

    SIM("Sim"),
    NAO("Não");

    private String descricao;
}
