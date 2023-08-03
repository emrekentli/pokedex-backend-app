package com.obss.pokedex.domain.authentication.user.impl;

import com.obss.pokedex.domain.authentication.role.api.RoleDto;
import com.obss.pokedex.domain.authentication.user.api.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
@RequiredArgsConstructor
public class DefaultUserCreator implements CommandLineRunner {

    @Value("${default.user.email}")
    private String email;
    @Value("${default.user.username}")
    private String username;
    @Value("${default.user.password}")
    private String password;

    private final UserRepository repository;
    private final UserServiceImpl service;
    private final PasswordEncoder encoder;


    @Override
    public void run(String... args) throws Exception {
        if (repository.findUserByUserName(username).isEmpty()) {
                    service.createUser(UserDto.builder()
                            .userName(username)
                            .password(encoder.encode(password))
                            .email(email)
                                    .fullName("Admin")
                            .roles(Set.of(RoleDto.builder().name("ROLE_ADMIN").displayName("Admin Rolü").build()))
                            .activity(true)
                            .phoneNumber("1234567890")
                            .build());
        }

    }
}