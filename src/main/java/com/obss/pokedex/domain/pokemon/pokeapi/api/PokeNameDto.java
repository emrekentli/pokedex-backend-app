package com.obss.pokedex.domain.pokemon.pokeapi.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PokeNameDto {
    private String name;
    private String url;
}
