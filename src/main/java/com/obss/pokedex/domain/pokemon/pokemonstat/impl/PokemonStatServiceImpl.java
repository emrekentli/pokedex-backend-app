package com.obss.pokedex.domain.pokemon.pokemonstat.impl;

import com.obss.pokedex.domain.pokemon.pokeapi.api.GetStatDto;
import com.obss.pokedex.domain.pokemon.pokemon.impl.Pokemon;
import com.obss.pokedex.domain.pokemon.pokemonstat.api.PokemonStatDto;
import com.obss.pokedex.domain.pokemon.pokemonstat.api.PokemonStatService;
import com.obss.pokedex.domain.pokemon.stat.impl.Stat;
import com.obss.pokedex.domain.pokemon.stat.impl.StatServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PokemonStatServiceImpl implements PokemonStatService {

    private final PokemonStatRepository repository;
    private final StatServiceImpl statService;

    public List<PokemonStat> convertToStats(List<GetStatDto> stats, Pokemon pokemon) {
        return stats.stream().map(stat -> convertToStat(stat,pokemon)).toList();
    }

    private PokemonStat convertToStat(GetStatDto stat, Pokemon pokemon) {
        PokemonStat statEntity = new PokemonStat();
        statEntity.setStatPoint(stat.getBaseStat());
        statEntity.setStat(statService.findStatByName(stat.getStat().getName()));
        statEntity.setPokemon(pokemon);
        return statEntity;
    }

    public List<PokemonStatDto> toDtoList(List<PokemonStat> stats) {
        return stats.stream().map(this::toDto).toList();
    }

    private PokemonStatDto toDto(PokemonStat pokemonStat) {
        return PokemonStatDto.builder()
                .id(pokemonStat.getId())
                .created(pokemonStat.getCreated())
                .modified(pokemonStat.getModified())
                .stat(statService.toDto(pokemonStat.getStat()))
                .statPoint(pokemonStat.getStatPoint())
                .build();
    }

    public PokemonStat createPokemonStat(Pokemon pokemon, Stat stat) {
        var pokemonStat = new PokemonStat();
        pokemonStat.setPokemon(pokemon);
        pokemonStat.setStat(stat);
        return repository.save(pokemonStat);
    }

    public PokemonStat getPokemonStatByPokemonAndStat(String statId) {
        return repository.findById(statId).orElseThrow(EntityNotFoundException::new);
    }
}
