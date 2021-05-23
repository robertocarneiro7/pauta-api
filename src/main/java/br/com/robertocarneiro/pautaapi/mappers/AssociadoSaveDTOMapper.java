package br.com.robertocarneiro.pautaapi.mappers;

import br.com.robertocarneiro.pautaapi.dtos.AssociadoSaveDTO;
import br.com.robertocarneiro.pautaapi.entities.Associado;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssociadoSaveDTOMapper extends DefaultMapper<Associado, AssociadoSaveDTO> {
}
