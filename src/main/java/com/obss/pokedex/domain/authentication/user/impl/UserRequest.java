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
    @NotBlank(message = "Password is required")
    private final String password;
    private final String fullName;
    private final String email;
    private final String phoneNumber;
    private final Boolean activity;


    public UserDto toDto() {
        return UserDto.builder()
                .userName(userName)
                .password(password)
                .fullName(fullName)
                .email(email)
                .phoneNumber(phoneNumber)
                .activity(activity)
                .build();
    }
}
