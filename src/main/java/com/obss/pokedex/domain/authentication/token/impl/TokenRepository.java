package com.obss.pokedex.domain.authentication.token.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@AllArgsConstructor
public class TokenRepository {

    public static final String HASH_KEY = "Token";
    private Map<String, String> tokenHashMap = new HashMap<>();

    public void save(String username, String token) {
        tokenHashMap.put(username, token);
    }

    public String getUserTokenByUserName(String username) {
        return tokenHashMap.get(username);
    }

    public void deleteUserTokenByUserName(String username) {
        tokenHashMap.remove(username);
    }

    public void updateUserToken(String username, String token) {
        tokenHashMap.put(username, token);
    }
}
