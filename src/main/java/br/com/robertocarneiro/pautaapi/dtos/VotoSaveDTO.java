package br.com.robertocarneiro.pautaapi.dtos;

import br.com.robertocarneiro.pautaapi.enums.EnumBoolean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotoSaveDTO {

    @NotBlank
    private String resposta;

    @JsonIgnore
    @AssertTrue(message = "{message.error.invalid-vote-option}")
    public boolean isValidOption() {
        return Stream.of(EnumBoolean.values()).anyMatch(e -> e.getDescricao().equals(resposta));
    }

}
