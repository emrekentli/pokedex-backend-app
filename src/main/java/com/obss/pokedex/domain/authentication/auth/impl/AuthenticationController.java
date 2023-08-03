package com.obss.pokedex.domain.authentication.auth.impl;
import com.obss.pokedex.domain.authentication.auth.api.AuthenticationService;
import com.obss.pokedex.domain.authentication.user.impl.UserRequest;
import com.obss.pokedex.library.rest.BaseController;
import com.obss.pokedex.library.rest.MetaResponse;
import com.obss.pokedex.library.rest.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController extends BaseController {

    private final AuthenticationService service;

    @PostMapping("/login")
    public Response<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return respond(service.login(request));
    }

    @PostMapping("/register")
    public Response<Void> register(@Valid @RequestBody UserRequest request) {
        service.register(request);
        return new Response<>(MetaResponse.ofSuccess());
    }


}
