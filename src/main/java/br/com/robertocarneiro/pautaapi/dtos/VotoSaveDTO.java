package br.com.robertocarneiro.pautaapi.dtos;

import br.com.robertocarneiro.pautaapi.enums.EnumBoolean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude
public class VotoSaveDTO {

    @NotBlank
    private String resposta;
    @NotNull
    private Long pautaId;
    @NotNull
    private Long associadoId;

    @JsonIgnore
    @AssertTrue(message = "{message.error.invalid-vote-option}")
    public boolean isValidOption() {
        return Stream.of(EnumBoolean.values()).anyMatch(e -> e.getDescricao().equals(resposta));
    }

}
