package com.obss.pokedex.domain.authentication.user.impl;

import com.obss.pokedex.domain.authentication.user.api.UserDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {
    @NotBlank(message = "UserName is required")
    private final String userName;
    private final String fullName;
    private final String email;
    private final String phoneNumber;


    public UserDto toDto() {
        return UserDto.builder()
                .userName(userName)
                .fullName(fullName)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
    }
}
