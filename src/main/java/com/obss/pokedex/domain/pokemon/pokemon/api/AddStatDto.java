package com.obss.pokedex.domain.pokemon.pokemon.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddStatDto {
    private String statId;
    private Integer statPoint;
}
