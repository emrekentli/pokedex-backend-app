package com.obss.pokedex.domain.authentication.auth.api;

import com.obss.pokedex.domain.authentication.auth.impl.AuthenticationRequest;
import com.obss.pokedex.domain.authentication.auth.impl.AuthenticationResponse;
import com.obss.pokedex.domain.authentication.user.impl.UserRequest;

public interface AuthenticationService {

    AuthenticationResponse login(AuthenticationRequest request);

    void register(UserRequest request);
}
