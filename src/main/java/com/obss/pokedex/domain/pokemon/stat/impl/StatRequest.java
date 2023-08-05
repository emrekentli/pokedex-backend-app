package com.obss.pokedex.domain.pokemon.stat.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.obss.pokedex.domain.pokemon.stat.api.StatDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatRequest {
    private final String name;

    public StatRequest(@JsonProperty("name") String name) {
        this.name = name;
    }
    public StatDto toDto() {
        return StatDto.builder()
                .name(name)
                .build();
    }
}
