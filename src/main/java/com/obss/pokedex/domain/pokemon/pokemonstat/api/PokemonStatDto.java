package com.obss.pokedex.domain.pokemon.pokemonstat.api;

import com.obss.pokedex.domain.pokemon.stat.api.StatDto;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PokemonStatDto {
    private final String id;
    private final Date created;
    private final Date modified;
    private final Integer statPoint;
    private final StatDto stat;
}
