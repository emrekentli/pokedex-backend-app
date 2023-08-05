package com.obss.pokedex.domain.pokemon.type.impl;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class TypeResponse {
    private final String id;
    private final Date created;
    private final Date modified;
    private final String name;
}
