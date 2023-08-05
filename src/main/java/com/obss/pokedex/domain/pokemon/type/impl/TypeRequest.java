package com.obss.pokedex.domain.pokemon.type.impl;

import com.obss.pokedex.domain.pokemon.type.api.TypeDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TypeRequest {
    private final String name;

    public TypeDto toDto() {
        return TypeDto.builder()
                .name(name)
                .build();
    }
}
