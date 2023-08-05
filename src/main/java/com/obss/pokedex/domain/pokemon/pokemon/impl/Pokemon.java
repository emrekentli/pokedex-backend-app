package com.obss.pokedex.domain.pokemon.pokemon.impl;


import com.obss.pokedex.domain.pokemon.ability.impl.Ability;
import com.obss.pokedex.domain.pokemon.pokemonstat.impl.PokemonStat;
import com.obss.pokedex.domain.pokemon.type.impl.Type;
import com.obss.pokedex.library.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Pokemon.TABLE)
public class Pokemon extends AbstractEntity {
    public static final String TABLE = "pokemon";
    public static final String COL_NAME= "name";
    public static final String COL_BASE_EXPERIENCE= "base_experience";
    public static final String COL_HEIGHT= "height";
    public static final String COL_WEIGHT= "weight";
    public static final String COL_IMAGE_URL= "image_url";

    @Column(name = COL_NAME)
    private String name;

    @Column(name = COL_BASE_EXPERIENCE)
    private Integer baseExperience;

    @Column(name = COL_HEIGHT)
    private Integer height;

    @Column(name = COL_WEIGHT)
    private Integer weight;

    @Column(name = COL_IMAGE_URL)
    private String imageUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "pokemon_type",
            joinColumns = @JoinColumn(name = "pokemon_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id"))
    private Set<Type> types;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "pokemon_ability",
            joinColumns = @JoinColumn(name = "pokemon_id"),
            inverseJoinColumns = @JoinColumn(name = "ability_id"))
    private Set<Ability> abilities;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "pokemon_pokemon_stat",
            joinColumns = @JoinColumn(name = "pokemon_id"),
            inverseJoinColumns = @JoinColumn(name = "pokemon_stat_id"))
    private Set<PokemonStat> stats;

}
