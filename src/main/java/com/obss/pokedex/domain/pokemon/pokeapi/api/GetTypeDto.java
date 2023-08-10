package com.obss.pokedex.domain.pokemon.pokeapi.api;

import lombok.Data;

@Data
public class GetTypeDto {
    private int slot;
    private PokeNameDto type;
}
