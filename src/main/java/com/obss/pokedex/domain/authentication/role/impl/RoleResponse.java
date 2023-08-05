package com.obss.pokedex.domain.authentication.role.impl;

import com.obss.pokedex.domain.authentication.role.api.RoleDto;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class RoleResponse {
    private final String id;
    private final Date created;
    private final Date modified;
    private final String name;
    private final String displayName;

    public static RoleResponse toResponse(RoleDto dto) {
        return RoleResponse.builder()
                .id(dto.getId())
                .created(dto.getCreated())
                .modified(dto.getModified())
                .name(dto.getName())
                .displayName(dto.getDisplayName())
                .build();
    }
}
