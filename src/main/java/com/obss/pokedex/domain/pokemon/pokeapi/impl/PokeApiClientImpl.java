package com.obss.pokedex.domain.pokemon.pokeapi.impl;

import com.obss.pokedex.domain.pokemon.pokeapi.api.GetPokemonDto;
import com.obss.pokedex.domain.pokemon.pokeapi.api.PokeApiClient;
import com.obss.pokedex.library.feign.PokeApiFeignCallableApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PokeApiClientImpl implements PokeApiClient {
    private final PokeApiFeignCallableApi callableApi;

    @Override
    public GetPokemonDto getPokemons(int offset, int limit) {
        return callableApi.getPokemons(offset, limit);
    }

    @Override
    public GetPokemonDto getAbilities(int offset, int limit) {
        return callableApi.getAbilities(offset, limit);
    }

    @Override
    public GetPokemonDto getTypes(int offset, int limit) {
        return callableApi.getTypes(offset, limit);
    }

    @Override
    public GetPokemonDto getStats(int offset, int limit) {
        return callableApi.getStats(offset, limit);
    }
}
