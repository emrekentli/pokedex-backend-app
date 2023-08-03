package com.obss.pokedex.domain.authentication.role.impl;

import com.obss.pokedex.domain.authentication.role.api.RoleDto;
import com.obss.pokedex.domain.authentication.user.api.UserDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleRequest {
    @NotBlank(message = "Name is required")
    private final String name;
    private final String displayName;


    public RoleDto toDto() {
        return RoleDto.builder()
                .name(name)
                .displayName(displayName)
                .build();
    }
}
