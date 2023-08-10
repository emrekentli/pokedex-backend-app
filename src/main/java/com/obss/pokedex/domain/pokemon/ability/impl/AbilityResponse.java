package com.obss.pokedex.domain.pokemon.ability.impl;

import com.obss.pokedex.domain.pokemon.ability.api.AbilityDto;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AbilityResponse {
    private final String id;
    private final Date created;
    private final Date modified;
    private final String name;

    public static AbilityResponse toResponse(AbilityDto ability) {
        return AbilityResponse.builder()
                .id(ability.getId())
                .created(ability.getCreated())
                .modified(ability.getModified())
                .name(ability.getName())
                .build();
    }
}
