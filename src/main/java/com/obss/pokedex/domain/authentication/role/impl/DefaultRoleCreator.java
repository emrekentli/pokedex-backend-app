package com.obss.pokedex.domain.authentication.role.impl;

import com.obss.pokedex.domain.authentication.role.api.RoleDto;
import com.obss.pokedex.domain.authentication.role.api.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DefaultRoleCreator implements CommandLineRunner {
    private final RoleRepository repository;
    private final RoleService service;

    @Override
    public void run(String... args) throws Exception {
        if (repository.findByName("ROLE_ADMIN").isEmpty()) {
            service.createRole(RoleDto.builder()
                    .name("ROLE_ADMIN")
                    .displayName("Admin Rolü")
                    .build());
        }
        if (repository.findByName("ROLE_USER").isEmpty()) {
            service.createRole(RoleDto.builder()
                    .name("ROLE_USER")
                    .displayName("User Rolü")
                    .build());
        }
    }
}