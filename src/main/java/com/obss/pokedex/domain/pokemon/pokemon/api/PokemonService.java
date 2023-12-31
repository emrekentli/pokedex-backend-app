package com.obss.pokedex.domain.pokemon.pokemon.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PokemonService {
    PokemonDto createPokemon(PokemonDto dto);

    PokemonDto updatePokemon(String id, PokemonDto dto);

    void deletePokemon(String id);

    Page<PokemonDto> getAll(Pageable pageable);

    PokemonDto getById(String id);

    Page<PokemonDto> filterPokemons(PokemonDto dto,String type,String ability, Pageable pageable);

    PokemonDto addType(String id, String typeId);

    PokemonDto removeType(String id, String typeId);

    PokemonDto addAbility(String id, String abilityId);

    PokemonDto removeAbility(String id, String abilityId);

    PokemonDto addStat(String id, AddStatDto dto);

    PokemonDto removeStat(String id, String statId);

    List<PokemonDto> getAllUserCatchlist(PokemonDto dto, String type, String ability);


    List<PokemonDto> getAllUserWishlist(PokemonDto dto, String type, String ability);
}
