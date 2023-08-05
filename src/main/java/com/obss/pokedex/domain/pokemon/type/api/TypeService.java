package com.obss.pokedex.domain.pokemon.type.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TypeService {
    TypeDto createType(TypeDto dto);

    TypeDto updateType(String id, TypeDto dto);

    void deleteType(String id);

    Page<TypeDto> getAll(Pageable pageable);

    TypeDto getById(String id);

    Page<TypeDto> filterTypes(TypeDto dto, Pageable pageable);
}
