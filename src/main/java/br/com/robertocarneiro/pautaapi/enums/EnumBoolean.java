package br.com.robertocarneiro.pautaapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumBoolean {

    SIM("Sim"),
    NAO("N�o");

    private String descricao;
}
