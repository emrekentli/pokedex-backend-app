package com.obss.pokedex.domain.pokemon.pokeapi.api;

public interface PokeApiClient {
    GetPokemonDto getPokemons(int offset, int limit);

    GetPokemonDto getAbilities(int offset, int limit);

    GetPokemonDto getTypes(int offset, int limit);

    GetPokemonDto getStats(int i, int i1);
}
