package com.obss.pokedex.domain.pokemon.pokemon.impl;

import com.obss.pokedex.domain.pokemon.pokemon.api.PokemonDto;
import com.obss.pokedex.domain.pokemon.pokemon.api.PokemonService;
import com.obss.pokedex.library.rest.BaseController;
import com.obss.pokedex.library.rest.MetaResponse;
import com.obss.pokedex.library.rest.PageResponse;
import com.obss.pokedex.library.rest.Response;
import com.obss.pokedex.library.util.PageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/pokemons")
@RequiredArgsConstructor
public class PokemonController extends BaseController {
    private final PokemonService service;

    @PostMapping(consumes = {"multipart/form-data"})
    public Response<PokemonResponse> createPokemon(@Valid @RequestPart("data") PokemonRequest request, @RequestPart("file") MultipartFile file ) {
        var pokemon = service.createPokemon(request.toDto(), file);
        return respond(PokemonResponse.toResponse(pokemon));
    }

    @PutMapping("/{id}")
    public Response<PokemonResponse> updatePokemon(@Valid @PathVariable(name = "id") String id,
                                                   @RequestBody PokemonRequest request) {
        var pokemon = service.updatePokemon(id, request.toDto());
        return respond(PokemonResponse.toResponse(pokemon));
    }
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

    @PostMapping("/{id}/types")
    public Response<PokemonResponse> addType(@PathVariable(name = "id") String id,@RequestParam(value = "typeId") String typeId) {
        PokemonDto pokemon = service.addType(id, typeId);
        return respond(PokemonResponse.toResponse(pokemon));
    }

    @DeleteMapping("/{id}/types")
    public Response<PokemonResponse> removeType(@PathVariable(name = "id") String id,@RequestParam(value = "typeId") String typeId) {
        PokemonDto pokemon = service.removeType(id, typeId);
        return respond(PokemonResponse.toResponse(pokemon));
    }

    @PostMapping("/{id}/abilities")
    public Response<PokemonResponse> addAbility(@PathVariable(name = "id") String id,@RequestParam(value = "abilityId") String abilityId) {
        PokemonDto pokemon = service.addAbility(id, abilityId);
        return respond(PokemonResponse.toResponse(pokemon));
    }

    @DeleteMapping("/{id}/abilities")
    public Response<PokemonResponse> removeAbility(@PathVariable(name = "id") String id,@RequestParam(value = "abilityId") String abilityId) {
        PokemonDto pokemon = service.removeAbility(id, abilityId);
        return respond(PokemonResponse.toResponse(pokemon));
    }

    @PostMapping("/{id}/stats")
    public Response<PokemonResponse> addStat(@PathVariable(name = "id") String id,@RequestBody AddStatRequest request) {
        PokemonDto pokemon = service.addStat(id, request.toDto());
        return respond(PokemonResponse.toResponse(pokemon));
    }

    @DeleteMapping("/{id}/stats")
    public Response<PokemonResponse> removeStat(@PathVariable(name = "id") String id,@RequestParam(value = "statId") String statId) {
            PokemonDto pokemon = service.removeStat(id, statId);
        return respond(PokemonResponse.toResponse(pokemon));
    }

    private Page<PokemonResponse> toPageResponse(Page<PokemonDto> pokemons) {
        return PageUtil.pageToDto(pokemons, PokemonResponse::toResponse);
    }
}
