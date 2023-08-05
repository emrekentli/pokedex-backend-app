package com.obss.pokedex.domain.pokemon.pokemon.impl;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PokemonRepository extends JpaRepository<Pokemon, String> {
}
