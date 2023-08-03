package com.obss.pokedex.domain.authentication.user.impl;

import com.obss.pokedex.domain.authentication.user.api.UserRetrievalService;
import com.obss.pokedex.domain.authentication.user.api.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRetrievalServiceImpl implements UserRetrievalService {

    /**
     * Returns the current user.
     *
     * @return The current user
     */
    @Override
    public String getCurrentUserId() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName)
                .orElseThrow(() -> new IllegalArgumentException("User id cannot be retrieved from JWT"));
    }
}
