package com.obss.pokedex.domain.pokemon.pokemon.impl;

import com.obss.pokedex.domain.pokemon.ability.api.AbilityDto;
import com.obss.pokedex.domain.pokemon.pokemon.api.PokemonDto;
import com.obss.pokedex.domain.pokemon.pokemonstat.api.PokemonStatDto;
import com.obss.pokedex.domain.pokemon.type.api.TypeDto;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class PokemonResponse {
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

    public static PokemonResponse toResponse(PokemonDto dto) {
        return PokemonResponse.builder()
                .id(dto.getId())
                .created(dto.getCreated())
                .modified(dto.getModified())
                .name(dto.getName())
                .baseExperience(dto.getBaseExperience())
                .height(dto.getHeight())
                .weight(dto.getWeight())
                .imageUrl(dto.getImageUrl())
                .types(dto.getTypes())
                .abilities(dto.getAbilities())
                .stats(dto.getStats())
                .build();
    }
}
