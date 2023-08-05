package com.obss.pokedex.domain.pokemon.pokemon.impl;

import com.obss.pokedex.domain.pokemon.pokeapi.api.GetPokemonDto;
import com.obss.pokedex.domain.pokemon.pokeapi.api.PokeApiClient;
import com.obss.pokedex.domain.pokemon.pokeapi.api.PokeNameDto;
import com.obss.pokedex.domain.pokemon.pokemon.api.PokemonService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PokemonServiceImpl implements PokemonService {
    private final PokeApiClient client;
    @PostConstruct
    public void init() {
        var totalCount = client.getPokemons(0, 20).getCount();
        if (repository.count() < totalCount) {
            var allPokemons = getAllPokemons();
            repository.saveAll(allPokemons.stream().map(this::toEntity).toList());
        }
    }

    private Pokemon toEntity(PokeNameDto pokeNameDto) {
        var pokemon = new Pokemon();
        pokemon.setName(pokeNameDto.getName());
        return pokemon;
    }

    public List<PokeNameDto> getAllPokemons() {
        List<PokeNameDto> allPokemons = new ArrayList<>();
        int offset = 0;
        int limit = 20;
        int totalPokemons = client.getPokemons(0,20).getCount();
        while (offset < totalPokemons) {
            GetPokemonDto response = client.getPokemons(offset, limit);
            allPokemons.addAll(response.getResults());
            offset += limit;
        }

        return allPokemons;
    }
    private final PokemonRepository repository;
}
