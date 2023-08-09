package com.obss.pokedex.domain.pokemon.pokemon.impl;

import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PokemonRepository extends JpaRepository<Pokemon, String> {
    Optional<Pokemon> findPokemonByName(String name);

    @Query("SELECT DISTINCT p FROM Pokemon p " +
            "LEFT JOIN p.types t " +
            "LEFT JOIN p.abilities a " +
            "LEFT JOIN p.stats s " +
            "WHERE " +
            "(:name IS NULL OR UPPER(p.name) LIKE CONCAT('%', UPPER(:name), '%')) AND " +
            "(:baseExperience IS NULL OR p.baseExperience = :baseExperience) AND " +
            "(:height IS NULL OR p.height = :height) AND " +
            "(:weight IS NULL OR p.weight = :weight) AND " +
            "(:type IS NULL OR t.name = :type) AND " +
            "(:ability IS NULL OR UPPER(a.name) LIKE CONCAT('%', UPPER(:ability), '%')) ")

    Page<Pokemon> filterPokemons(
            @Param("name") String name,
            @Param("baseExperience") Integer baseExperience,
            @Param("height") Double height,
            @Param("weight") Double weight,
            @Param("type") String type,
            @Param("ability") String ability,
            Pageable pageable
    );

    @Query("SELECT DISTINCT p FROM Pokemon p " +
            "LEFT JOIN p.types t " +
            "LEFT JOIN p.abilities a " +
            "LEFT JOIN p.stats s " +
            "WHERE " +
            "(:name IS NULL OR UPPER(p.name) LIKE CONCAT('%', UPPER(:name), '%')) AND " +
            "(:baseExperience IS NULL OR p.baseExperience = :baseExperience) AND " +
            "(:height IS NULL OR p.height = :height) AND " +
            "(:weight IS NULL OR p.weight = :weight) AND " +
            "(:type IS NULL OR t.name = :type) AND " +
            "(:ability IS NULL OR UPPER(a.name) LIKE CONCAT('%', UPPER(:ability), '%')) AND " +
            "(p.id IN :catchlist)")
    List<Pokemon> findAllByIdIn(String name, Integer baseExperience, Double height, Double weight, String type, String ability, List<String> catchlist);
}
