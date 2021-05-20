package br.com.robertocarneiro.pautaapi.mappers;

import br.com.robertocarneiro.pautaapi.dtos.PautaSaveDTO;
import br.com.robertocarneiro.pautaapi.entities.Pauta;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PautaSaveDTOMapper extends DefaultMapper<Pauta, PautaSaveDTO> {
}
