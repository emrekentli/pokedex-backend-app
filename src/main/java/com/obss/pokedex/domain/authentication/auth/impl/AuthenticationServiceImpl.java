package com.obss.pokedex.domain.authentication.auth.impl;

import com.obss.pokedex.domain.authentication.auth.api.AuthenticationService;
import com.obss.pokedex.domain.authentication.role.impl.Role;
import com.obss.pokedex.domain.authentication.role.impl.RoleServiceImpl;
import com.obss.pokedex.domain.authentication.token.impl.TokenRepository;
import com.obss.pokedex.domain.authentication.user.impl.User;
import com.obss.pokedex.domain.authentication.user.impl.UserRequest;
import com.obss.pokedex.domain.authentication.user.impl.UserServiceImpl;
import com.obss.pokedex.library.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userService;
    private final TokenRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RoleServiceImpl roleService;

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        var user = userService.getUserByUserName(request.getUserName());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getId(), request.getPassword())
        );
        var token = jwtUtil.generateToken(userService.toDto(user));
        repository.save(user.getId(), token);
        return AuthenticationResponse.builder()
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .token(token)
                .build();
    }

    @Transactional
    @Override
    public void register(UserRequest request) {
        userService.checkUserExists(request.getUserName());
        var user = userService.toEntity(new User(), request.toDto());
        user.setActivity(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roleService.getRolesByRoleNames(Set.of("ROLE_USER")));
        userService.saveUser(user);
    }
}
