package com.obss.pokedex.domain.pokemon.pokemon.impl;

import com.obss.pokedex.domain.pokemon.pokemon.api.PokemonDto;
import com.obss.pokedex.domain.pokemon.pokemon.api.PokemonService;
import com.obss.pokedex.library.rest.*;
import com.obss.pokedex.library.util.PageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pokemons")
@RequiredArgsConstructor
public class PokemonController extends BaseController {
    private final PokemonService service;
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @PostMapping
    public Response<PokemonResponse> createPokemon(@Valid @RequestBody PokemonRequest request) {
        var pokemon = service.createPokemon(request.toDto());
        return respond(PokemonResponse.toResponse(pokemon));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @PutMapping("/{id}")
    public Response<PokemonResponse> updatePokemon(@Valid @PathVariable(name = "id") String id,
                                                   @RequestBody PokemonRequest request) {
        var pokemon = service.updatePokemon(id, request.toDto());
        return respond(PokemonResponse.toResponse(pokemon));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @DeleteMapping("/{id}")
    public Response<Void> deletePokemon(@PathVariable(name = "id") String id) {
        service.deletePokemon(id);
        return new Response<>(MetaResponse.ofSuccess());
    }

    @GetMapping
    public Response<PageResponse<PokemonResponse>> getAll(Pageable pageable) {
        return respond(toPageResponse(service.getAll(pageable)));
    }

    @GetMapping("/{id}")
    public Response<PokemonResponse> getById(@PathVariable(value = "id") String id) {
        return respond(PokemonResponse.toResponse(service.getById(id)));
    }
    @GetMapping("/filter")
    public Response<PageResponse<PokemonResponse>> filterPokemons(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double height,
            @RequestParam(required = false) Double weight,
            @RequestParam(required = false) String ability,
            @RequestParam(required = false) Integer baseExperience,
            Pageable pageable) {

        PokemonDto dto = PokemonDto.builder()
                .name(name)
                .height(height)
                .weight(weight)
                .baseExperience(baseExperience)
                .build();
        return respond(toPageResponse(service.filterPokemons(dto, type,ability, pageable)));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @PostMapping("/{id}/types")
    public Response<PokemonResponse> addType(@PathVariable(name = "id") String id,@RequestParam(value = "typeId") String typeId) {
        PokemonDto pokemon = service.addType(id, typeId);
        return respond(PokemonResponse.toResponse(pokemon));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @DeleteMapping("/{id}/types")
    public Response<PokemonResponse> removeType(@PathVariable(name = "id") String id,@RequestParam(value = "typeId") String typeId) {
        PokemonDto pokemon = service.removeType(id, typeId);
        return respond(PokemonResponse.toResponse(pokemon));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @PostMapping("/{id}/abilities")
    public Response<PokemonResponse> addAbility(@PathVariable(name = "id") String id,@RequestParam(value = "abilityId") String abilityId) {
        PokemonDto pokemon = service.addAbility(id, abilityId);
        return respond(PokemonResponse.toResponse(pokemon));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @DeleteMapping("/{id}/abilities")
    public Response<PokemonResponse> removeAbility(@PathVariable(name = "id") String id,@RequestParam(value = "abilityId") String abilityId) {
        PokemonDto pokemon = service.removeAbility(id, abilityId);
        return respond(PokemonResponse.toResponse(pokemon));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @PostMapping("/{id}/stats")
    public Response<PokemonResponse> addStat(@PathVariable(name = "id") String id,@RequestBody AddStatRequest request) {
        PokemonDto pokemon = service.addStat(id, request.toDto());
        return respond(PokemonResponse.toResponse(pokemon));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @DeleteMapping("/{id}/stats")
    public Response<PokemonResponse> removeStat(@PathVariable(name = "id") String id,@RequestParam(value = "statId") String statId) {
            PokemonDto pokemon = service.removeStat(id, statId);
        return respond(PokemonResponse.toResponse(pokemon));
    }
    @GetMapping("/catchlist")
    public Response<DataResponse<PokemonResponse>> getAllUserCatchlist(@RequestParam(required = false) String name,
                                                                       @RequestParam(required = false) String type,
                                                                       @RequestParam(required = false) Double height,
                                                                       @RequestParam(required = false) Double weight,
                                                                       @RequestParam(required = false) String ability,
                                                                       @RequestParam(required = false) Integer baseExperience,
                                                                       Pageable pageable) {
        PokemonDto dto = PokemonDto.builder()
                .name(name)
                .height(height)
                .weight(weight)
                .baseExperience(baseExperience)
                .build();
        return respond(toResponse(service.getAllUserCatchlist(dto,type,ability)));
    }

    @GetMapping("/wishlist")
    public Response<DataResponse<PokemonResponse>> getAllUserWishlist(@RequestParam(required = false) String name,
                                                                      @RequestParam(required = false) String type,
                                                                      @RequestParam(required = false) Double height,
                                                                      @RequestParam(required = false) Double weight,
                                                                      @RequestParam(required = false) String ability,
                                                                      @RequestParam(required = false) Integer baseExperience) {
        PokemonDto dto = PokemonDto.builder()
                .name(name)
                .height(height)
                .weight(weight)
                .baseExperience(baseExperience)
                .build();
        return respond(toResponse(service.getAllUserWishlist(dto,type,ability)));
    }

    private Page<PokemonResponse> toPageResponse(Page<PokemonDto> pokemons) {
        return PageUtil.pageToDto(pokemons, PokemonResponse::toResponse);
    }
    private List<PokemonResponse> toResponse(List<PokemonDto> pokemons) {
        return pokemons.stream().map(PokemonResponse::toResponse).toList();
    }
}
