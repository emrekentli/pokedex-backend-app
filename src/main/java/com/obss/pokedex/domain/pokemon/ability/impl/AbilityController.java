package com.obss.pokedex.domain.pokemon.ability.impl;

import com.obss.pokedex.domain.pokemon.ability.api.AbilityDto;
import com.obss.pokedex.domain.pokemon.ability.api.AbilityService;
import com.obss.pokedex.library.rest.BaseController;
import com.obss.pokedex.library.rest.MetaResponse;
import com.obss.pokedex.library.rest.PageResponse;
import com.obss.pokedex.library.rest.Response;
import com.obss.pokedex.library.util.PageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/abilities")
@RequiredArgsConstructor
public class AbilityController extends BaseController {
    private final AbilityService service;
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @PostMapping
    public Response<AbilityResponse> createAbility(@Valid @RequestBody AbilityRequest request) {
        var ability = service.createAbility(request.toDto());
        return respond(AbilityResponse.toResponse(ability));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @PutMapping("/{id}")
    public Response<AbilityResponse> updateAbility(@Valid @PathVariable(name = "id") String id,
                                             @RequestBody AbilityRequest request) {
        var ability = service.updateAbility(id, request.toDto());
        return respond(AbilityResponse.toResponse(ability));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @DeleteMapping("/{id}")
    public Response<Void> deleteAbility(@PathVariable(name = "id") String id) {
        service.deleteAbility(id);
        return new Response<>(MetaResponse.ofSuccess());
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @GetMapping
    public Response<PageResponse<AbilityResponse>> getAll(Pageable pageable) {
        return respond(toPageResponse(service.getAll(pageable)));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @GetMapping("/{id}")
    public Response<AbilityResponse> getById(@PathVariable(value = "id") String id) {
        return respond(AbilityResponse.toResponse(service.getById(id)));
    }

    @GetMapping("/filter")
    public Response<PageResponse<AbilityResponse>> filterAbilites(
            @RequestParam(required = false) String name,
            Pageable pageable) {

        AbilityDto dto = AbilityDto.builder()
                .name(name)
                .build();
        return respond(toPageResponse(service.filterAbilites(dto, pageable)));
    }



    private Page<AbilityResponse> toPageResponse(Page<AbilityDto> abilityDtos) {
        return PageUtil.pageToDto(abilityDtos, AbilityResponse::toResponse);
    }
}
