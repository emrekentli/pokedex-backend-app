package com.obss.pokedex.domain.pokemon.pokemonstat.impl;

import com.obss.pokedex.domain.pokemon.pokemonstat.api.PokemonStatDto;
import com.obss.pokedex.domain.pokemon.stat.api.StatDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PokemonStatRequest {

    private final int statPoint;
    private final String statId;

    public PokemonStatDto toDto() {
        return PokemonStatDto.builder()
                .statPoint(statPoint)
                .stat(StatDto.builder().id(statId).build())
                .build();
    }
}
