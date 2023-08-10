package com.obss.pokedex.domain.authentication.user.impl;

import com.obss.pokedex.domain.authentication.role.api.RoleDto;
import com.obss.pokedex.domain.authentication.user.api.UserDto;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Builder
public class UserResponse {
    private final String id;
    private final Date created;
    private final Date modified;
    private final String userName;
    private final String password;
    private final String fullName;
    private final String email;
    private final String phoneNumber;
    private final Boolean activity;
    private final Set<RoleDto> roles;

    public static UserResponse toResponse(UserDto dto) {
        return UserResponse.builder()
                .id(dto.getId())
                .created(dto.getCreated())
                .modified(dto.getModified())
                .userName(dto.getUserName())
                .password(dto.getPassword())
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .activity(dto.getActivity())
                .roles(dto.getRoles())
                .build();
    }
}
