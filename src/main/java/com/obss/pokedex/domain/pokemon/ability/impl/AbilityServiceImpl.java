package com.obss.pokedex.domain.pokemon.ability.impl;

import com.obss.pokedex.domain.pokemon.ability.api.AbilityDto;
import com.obss.pokedex.domain.pokemon.ability.api.AbilityService;
import com.obss.pokedex.domain.pokemon.pokeapi.api.GetAbilityDto;
import com.obss.pokedex.domain.pokemon.pokeapi.api.GetPokemonDto;
import com.obss.pokedex.domain.pokemon.pokeapi.api.PokeApiClient;
import com.obss.pokedex.domain.pokemon.pokeapi.api.PokeNameDto;
import com.obss.pokedex.library.util.PageUtil;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    public AbilityDto createAbility(AbilityDto dto) {
        return toDtoList(repository.save(toEntity(new Ability(),dto)));
    }

    @Override
    public AbilityDto updateAbility(String id, AbilityDto dto) {
        return toDtoList(repository.save(toEntity(getEntityById(id),dto)));
    }

    @Override
    public void deleteAbility(String id) {
        repository.delete(getEntityById(id));
    }

    @Override
    public Page<AbilityDto> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDtoList);
    }

    @Override
    public AbilityDto getById(String id) {
        return toDtoList(getEntityById(id));
    }

    @Override
    public Page<AbilityDto> filterAbilites(AbilityDto dto, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
        return PageUtil.pageToDto(repository.findAll(Example.of(toEntity(new Ability(), dto),matcher), pageable), this::toDtoList);
    }

    public Ability getEntityById(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Ability not found with id: " + id));
    }

    private List<PokeNameDto> getAllAbilities() {
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

    public Set<Ability> convertToAbilities(List<GetAbilityDto> abilities) {
        return abilities.stream().map(ability -> convertToAbility(ability)).collect(Collectors.toSet());
    }

    private Ability convertToAbility(GetAbilityDto ability) {
        var abilityEntity = repository.findByName(ability.getAbility().getName()).orElse(null);
        if (abilityEntity == null) {
            abilityEntity = new Ability();
            abilityEntity.setName(ability.getAbility().getName());
        }
        return abilityEntity;
    }

    private Ability toEntity(PokeNameDto pokeNameDto) {
        var ability = new Ability();
        ability.setName(pokeNameDto.getName());
        return ability;
    }

    private Ability toEntity(Ability ability,AbilityDto dto) {
        ability.setName(dto.getName());
        return ability;
    }
    private AbilityDto toDtoList(Ability ability) {
        return AbilityDto.builder()
                .id(ability.getId())
                .created(ability.getCreated())
                .modified(ability.getModified())
                .name(ability.getName())
                .build();
    }

    public Set<AbilityDto> toDtoList(Set<Ability> abilities) {
        return abilities.stream().map(this::toDtoList).collect(Collectors.toSet());
    }
}
