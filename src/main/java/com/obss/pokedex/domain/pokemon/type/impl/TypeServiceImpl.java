package com.obss.pokedex.domain.pokemon.type.impl;

import com.obss.pokedex.domain.pokemon.pokeapi.api.GetPokemonDto;
import com.obss.pokedex.domain.pokemon.pokeapi.api.PokeApiClient;
import com.obss.pokedex.domain.pokemon.pokeapi.api.PokeNameDto;
import com.obss.pokedex.domain.pokemon.type.api.TypeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeServiceImpl implements TypeService {
    private final TypeRepository repository;
    private final PokeApiClient client;

    @PostConstruct
    public void init() {
        var totalCount = client.getTypes(0, 20).getCount();
        if (repository.count() < totalCount) {
            var allTypes = getAllTypes();
            repository.saveAll(allTypes.stream().map(this::toEntity).toList());
        }
    }

    private Type toEntity(PokeNameDto pokeNameDto) {
        var ability = new Type();
        ability.setName(pokeNameDto.getName());
        return ability;
    }

    public List<PokeNameDto> getAllTypes() {
        List<PokeNameDto> allTypes = new ArrayList<>();
        int offset = 0;
        int limit = 20;
        int totalPokemons = client.getTypes(0,20).getCount();
        while (offset < totalPokemons) {
            GetPokemonDto response = client.getTypes(offset, limit);
            allTypes.addAll(response.getResults());
            offset += limit;
        }

        return allTypes;
    }
}
