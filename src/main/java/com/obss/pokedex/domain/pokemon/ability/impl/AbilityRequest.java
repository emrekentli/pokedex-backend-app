package com.obss.pokedex.domain.pokemon.ability.impl;

import com.obss.pokedex.domain.pokemon.ability.api.AbilityDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AbilityRequest {
    private final String name;

    public AbilityDto toDto() {
        return AbilityDto.builder()
                .name(name)
                .build();
    }
}
