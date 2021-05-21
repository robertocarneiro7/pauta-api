package br.com.robertocarneiro.pautaapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VoteSituationStatus {

    ABLE_TO_VOTE(true),
    UNABLE_TO_VOTE(false);

    private boolean enable;
}
