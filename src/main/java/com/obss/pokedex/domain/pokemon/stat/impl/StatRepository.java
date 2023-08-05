package com.obss.pokedex.domain.pokemon.stat.impl;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StatRepository extends JpaRepository<Stat, String> {
}
