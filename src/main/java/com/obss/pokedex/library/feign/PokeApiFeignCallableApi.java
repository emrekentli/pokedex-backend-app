package com.obss.pokedex.library.feign;

import com.obss.pokedex.domain.pokemon.pokeapi.api.GetPokemonDetailDto;
import com.obss.pokedex.domain.pokemon.pokeapi.api.GetPokemonDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


public interface PokeApiFeignCallableApi {
    @GetMapping("/pokemon")
    GetPokemonDto getPokemons(@RequestParam("offset") int offset, @RequestParam("limit") int limit);
    @GetMapping("/ability")
    GetPokemonDto getAbilities(@RequestParam("offset") int offset, @RequestParam("limit") int limit);
    @GetMapping("/type")
    GetPokemonDto getTypes(@RequestParam("offset") int offset, @RequestParam("limit") int limit);
    @GetMapping("/stat")
    GetPokemonDto getStats(@RequestParam("offset") int offset, @RequestParam("limit") int limit);

    @GetMapping("/pokemon/{pokemonName}")
    GetPokemonDetailDto getPokemon(@PathVariable("pokemonName") String pokemonName);
}
