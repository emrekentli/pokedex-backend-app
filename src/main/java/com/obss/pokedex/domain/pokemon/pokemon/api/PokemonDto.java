package com.obss.pokedex.domain.pokemon.pokemon.api;

import com.obss.pokedex.domain.pokemon.ability.api.AbilityDto;
import com.obss.pokedex.domain.pokemon.pokemonstat.api.PokemonStatDto;
import com.obss.pokedex.domain.pokemon.type.api.TypeDto;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class PokemonDto {
    private final String id;
    private final Date created;
    private final Date modified;
    private final String name;
    private final Integer baseExperience;
    private final Double height;
    private final Double weight;
    private final String imageUrl;
    private Set<TypeDto> types;
    private Set<AbilityDto> abilities;
    private List<PokemonStatDto> stats;
}
