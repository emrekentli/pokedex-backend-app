package com.obss.pokedex.domain.authentication.role.api;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class RoleDto {
    private final String id;
    private final Date created;
    private final Date modified;
    private final String name;
    private final String displayName;
}
