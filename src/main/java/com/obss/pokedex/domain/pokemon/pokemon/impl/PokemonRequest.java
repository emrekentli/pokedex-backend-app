package com.obss.pokedex.domain.pokemon.pokemon.impl;

import com.obss.pokedex.domain.pokemon.pokemon.api.PokemonDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PokemonRequest {
    private final String name;

    public PokemonDto toDto() {
        return PokemonDto.builder()
                .name(name)
                .build();
    }
}
