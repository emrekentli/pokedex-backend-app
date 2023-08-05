package com.obss.pokedex.domain.pokemon.ability.impl;

import com.obss.pokedex.domain.pokemon.ability.api.AbilityService;
import com.obss.pokedex.domain.pokemon.pokeapi.api.GetPokemonDto;
import com.obss.pokedex.domain.pokemon.pokeapi.api.PokeApiClient;
import com.obss.pokedex.domain.pokemon.pokeapi.api.PokeNameDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AbilityServiceImpl implements AbilityService {
    private final AbilityRepository repository;
    private final PokeApiClient client;

    @PostConstruct
    public void init() {
        var totalCount = client.getAbilities(0, 20).getCount();
        if (repository.count() < totalCount) {
            var allAbilities = getAllAbilities();
            repository.saveAll(allAbilities.stream().map(this::toEntity).toList());
        }
    }

    private Ability toEntity(PokeNameDto pokeNameDto) {
        var ability = new Ability();
        ability.setName(pokeNameDto.getName());
        return ability;
    }

    public List<PokeNameDto> getAllAbilities() {
        List<PokeNameDto> allAbilities = new ArrayList<>();
        int offset = 0;
        int limit = 20;
        int totalPokemons = client.getAbilities(0,20).getCount();
        while (offset < totalPokemons) {
            GetPokemonDto response = client.getAbilities(offset, limit);
            allAbilities.addAll(response.getResults());
            offset += limit;
        }

        return allAbilities;
    }
}
