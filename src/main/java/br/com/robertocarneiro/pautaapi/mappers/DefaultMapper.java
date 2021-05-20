package br.com.robertocarneiro.pautaapi.mappers;

public interface DefaultMapper<E, D> {

    E dtoToEntity(D dto);
    D entityToDTO(E entity);
}
