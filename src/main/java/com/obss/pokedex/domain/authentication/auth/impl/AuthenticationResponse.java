package com.obss.pokedex.domain.authentication.auth.impl;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuthenticationResponse {
    private List<String> roles;
    private String token;
}
