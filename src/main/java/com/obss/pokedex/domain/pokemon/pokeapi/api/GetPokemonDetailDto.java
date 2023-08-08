package com.obss.pokedex.domain.pokemon.pokeapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetPokemonDetailDto {
    private int id;
    private String name;
    private List<GetAbilityDto> abilities;
    @JsonProperty("base_experience")
    private int baseExperience;
    private List<PokeNameDto> forms;
    private double height;
    private SpritesDto sprites;
    private List<GetStatDto> stats;
    private double weight;
    private List<GetTypeDto> types;
}
