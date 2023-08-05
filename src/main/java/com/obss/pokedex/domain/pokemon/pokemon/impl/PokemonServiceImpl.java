package com.obss.pokedex.domain.pokemon.pokemon.impl;

import com.obss.pokedex.domain.pokemon.ability.impl.AbilityServiceImpl;
import com.obss.pokedex.domain.pokemon.pokeapi.api.GetPokemonDetailDto;
import com.obss.pokedex.domain.pokemon.pokeapi.api.GetPokemonDto;
import com.obss.pokedex.domain.pokemon.pokeapi.api.PokeApiClient;
import com.obss.pokedex.domain.pokemon.pokeapi.api.PokeNameDto;
import com.obss.pokedex.domain.pokemon.pokemon.api.PokemonDto;
import com.obss.pokedex.domain.pokemon.pokemon.api.PokemonService;
import com.obss.pokedex.domain.pokemon.pokemonstat.impl.PokemonStatServiceImpl;
import com.obss.pokedex.domain.pokemon.stat.impl.Stat;
import com.obss.pokedex.domain.pokemon.stat.impl.StatServiceImpl;
import com.obss.pokedex.domain.pokemon.type.impl.TypeServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Log4j2
public class PokemonServiceImpl implements PokemonService {
    private final PokeApiClient client;
    private final AbilityServiceImpl abilityService;
    private final PokemonRepository repository;
    private final TypeServiceImpl typeService;
    private final PokemonStatServiceImpl pokemonStatService;
    private final StatServiceImpl statService;
    @PostConstruct
    public void init() {
        var totalCount = client.getPokemons(0, 100000).getCount();
        AtomicInteger currentCount = new AtomicInteger();

        if (repository.count() < totalCount) {
            var allPokemons = getAllPokemons();
            List<GetPokemonDetailDto> pokemonDetailsList = new ArrayList<>();
            allPokemons.forEach(pokemon -> {
                currentCount.getAndIncrement();
                log.info("Getting pokemon details: {} - Progress: {}/{}", pokemon.getName(), currentCount, totalCount);
                var pokemonDetails = client.getPokemonDetails(pokemon.getName());
                pokemonDetailsList.add(pokemonDetails);
            });
            saveBulkPokemonDetails(pokemonDetailsList);
        }
    }


    private void saveBulkPokemonDetails(List<GetPokemonDetailDto> pokemonDetailsList) {
       List<Pokemon> pokemonsWithDetails = new ArrayList<>();
        for (GetPokemonDetailDto pokemonDetails : pokemonDetailsList) {
            Pokemon pokemon = repository.findPokemonByName(pokemonDetails.getName()).orElse(new Pokemon());
            pokemon.setName(pokemonDetails.getName());
            pokemon.setBaseExperience(pokemonDetails.getBaseExperience());
            pokemon.setHeight(pokemonDetails.getHeight());
            pokemon.setWeight(pokemonDetails.getWeight());
            pokemon.setImageUrl(pokemonDetails.getSprites().getOther().getDreamWorld().getFrontDefault());
            pokemon.setAbilities(abilityService.convertToAbilities(pokemonDetails.getAbilities()));
            pokemon.setStats(pokemonStatService.convertToStats(pokemonDetails.getStats(),pokemon));
            pokemon.setTypes(typeService.convertToTypes(pokemonDetails.getTypes()));
            pokemonsWithDetails.add(pokemon);
            log.info("Creating pokemon : {}", pokemon.getName());
        }
        repository.saveAll(pokemonsWithDetails);
        log.info("Bulk pokemon details saved");
    }

    @Override
    public PokemonDto createPokemon(PokemonDto dto) {
        return toDto(repository.save(toEntity(new Pokemon(),dto)));
    }

    @Override
    public PokemonDto updatePokemon(String id, PokemonDto dto) {
        return toDto(repository.save(toEntity(repository.findById(id).orElseThrow(EntityNotFoundException::new),dto)));
    }

    @Override
    public void deletePokemon(String id) {
        repository.delete(repository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public Page<PokemonDto> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDto);
    }

    @Override
    public PokemonDto getById(String id) {
        return toDto(repository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public Page<PokemonDto> filterPokemons(PokemonDto dto, String type, String ability, Pageable pageable) {
        return repository.filterPokemons(dto.getName(),dto.getBaseExperience(),dto.getHeight(),dto.getWeight(),type,ability,pageable).map(this::toDto);
    }

    @Override
    public PokemonDto addType(String id, String typeId) {
        Pokemon pokemon = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        pokemon.getTypes().add(typeService.getEntityById(typeId));
        return toDto(repository.save(pokemon));
    }

    @Override
    public PokemonDto removeType(String id, String typeId) {
        Pokemon pokemon = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        pokemon.getTypes().remove(typeService.getEntityById(typeId));
        return toDto(repository.save(pokemon));
    }

    @Override
    public PokemonDto addAbility(String id, String abilityId) {
        Pokemon pokemon = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        pokemon.getAbilities().add(abilityService.getEntityById(abilityId));
        return toDto(repository.save(pokemon));
    }

    @Override
    public PokemonDto removeAbility(String id, String abilityId) {
        Pokemon pokemon = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        pokemon.getAbilities().remove(abilityService.getEntityById(abilityId));
        return toDto(repository.save(pokemon));
    }

    @Override
    public PokemonDto addStat(String id, String statId) {
        var pokemon = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        var stat = statService.getEntityById(statId);
        pokemon.getStats().add(pokemonStatService.createPokemonStat(pokemon,stat));
        return toDto(repository.save(pokemon));
    }

    @Override
    public PokemonDto removeStat(String id, String statId) {
        Pokemon pokemon = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        Stat stat = statService.getEntityById(statId);
        pokemon.getStats().remove(pokemonStatService.getPokemonStatByPokemonAndStat(pokemon,stat));
        return toDto(repository.save(pokemon));
    }

    private PokemonDto toDto(Pokemon pokemon) {
        return PokemonDto.builder()
                .id(pokemon.getId())
                .name(pokemon.getName())
                .baseExperience(pokemon.getBaseExperience())
                .height(pokemon.getHeight())
                .weight(pokemon.getWeight())
                .imageUrl(pokemon.getImageUrl())
                .abilities(abilityService.toDtoList(pokemon.getAbilities()))
                .stats(pokemonStatService.toDtoList(pokemon.getStats()))
                .types(typeService.toDtoList(pokemon.getTypes()))
                .build();
    }
    private Pokemon toEntity(Pokemon pokemon, PokemonDto dto) {
        pokemon.setName(dto.getName());
        pokemon.setBaseExperience(dto.getBaseExperience());
        pokemon.setHeight(dto.getHeight());
        pokemon.setWeight(dto.getWeight());
        pokemon.setImageUrl(dto.getImageUrl());
        return pokemon;
    }

    private List<PokeNameDto> getAllPokemons() {
        List<PokeNameDto> allPokemons = new ArrayList<>();
        int offset = 0;
        int limit = 100000;
        int totalPokemons = client.getPokemons(offset,limit).getCount();
        while (offset < totalPokemons) {
            GetPokemonDto response = client.getPokemons(offset, limit);
            allPokemons.addAll(response.getResults());
            offset += limit;
        }

        return allPokemons;
    }
}