package com.obss.pokedex.domain.pokemon.stat.impl;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatRepository extends JpaRepository<Stat, String> {
    Optional<Stat> findByName(String name);
}
