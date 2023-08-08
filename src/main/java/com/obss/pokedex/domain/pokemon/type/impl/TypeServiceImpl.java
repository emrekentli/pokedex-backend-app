package com.obss.pokedex.domain.pokemon.type.impl;

import com.obss.pokedex.domain.pokemon.pokeapi.api.GetPokemonDto;
import com.obss.pokedex.domain.pokemon.pokeapi.api.GetTypeDto;
import com.obss.pokedex.domain.pokemon.pokeapi.api.PokeApiClient;
import com.obss.pokedex.domain.pokemon.pokeapi.api.PokeNameDto;
import com.obss.pokedex.domain.pokemon.type.api.TypeDto;
import com.obss.pokedex.domain.pokemon.type.api.TypeService;
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

    private Type toEntity(Type type , TypeDto dto) {
        type.setName(dto.getName());
        return type;
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

    public Set<Type> convertToTypes(List<GetTypeDto> types) {
        return types.stream().map(type -> convertToType(type)).collect(Collectors.toSet());
    }

    private Type convertToType(GetTypeDto type) {
        var typeEntity = repository.findByName(type.getType().getName()).orElse(null);
        if (typeEntity == null) {
            typeEntity = new Type();
            typeEntity.setName(type.getType().getName());
        }
        return typeEntity;
    }

    public Set<TypeDto> toDtoList(Set<Type> types) {
        return types.stream().map(type -> toDto(type)).collect(Collectors.toSet());
    }

    private TypeDto toDto(Type type) {
        return TypeDto.builder()
                .id(type.getId())
                .created(type.getCreated())
                .modified(type.getModified())
                .name(type.getName())
                .build();
    }

    public Type getEntityById(String typeId) {
        return repository.findById(typeId).orElseThrow(() -> new EntityNotFoundException("Type not found with id: " + typeId));
    }

    @Override
    public TypeDto createType(TypeDto dto) {
        return toDto(repository.save(toEntity(new Type(),dto)));
    }

    @Override
    public TypeDto updateType(String id, TypeDto dto) {
        var type = getEntityById(id);
        return toDto(repository.save(toEntity(type,dto)));
    }

    @Override
    public void deleteType(String id) {
        repository.delete(getEntityById(id));
    }

    @Override
    public Page<TypeDto> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDto);
    }

    @Override
    public TypeDto getById(String id) {
        return toDto(getEntityById(id));
    }

    @Override
    public Page<TypeDto> filterTypes(TypeDto dto, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
        return PageUtil.pageToDto(repository.findAll(Example.of(toEntity(new Type(), dto),matcher), pageable), this::toDto);
    }
}
