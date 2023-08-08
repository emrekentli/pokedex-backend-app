package com.obss.pokedex.domain.pokemon.type.impl;

import com.obss.pokedex.domain.pokemon.type.api.TypeDto;
import com.obss.pokedex.domain.pokemon.type.api.TypeService;
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
@RequestMapping("/types")
@RequiredArgsConstructor
public class TypeController extends BaseController {
    private final TypeService service;
    
    @PostMapping
    public Response<TypeResponse> createType(@Valid @RequestBody TypeRequest request) {
        var type = service.createType(request.toDto());
        return respond(TypeResponse.toResponse(type));
    }
    
    @PutMapping("/{id}")
    public Response<TypeResponse> updateType(@Valid @PathVariable(name = "id") String id,
                                             @RequestBody TypeRequest request) {
        var type = service.updateType(id, request.toDto());
        return respond(TypeResponse.toResponse(type));
    }
    
    @DeleteMapping("/{id}")
    public Response<Void> deleteType(@PathVariable(name = "id") String id) {
        service.deleteType(id);
        return new Response<>(MetaResponse.ofSuccess());
    }
    
    @GetMapping
    public Response<PageResponse<TypeResponse>> getAll(Pageable pageable) {
        return respond(toPageResponse(service.getAll(pageable)));
    }

    
    @GetMapping("/{id}")
    public Response<TypeResponse> getById(@PathVariable(value = "id") String id) {
        return respond(TypeResponse.toResponse(service.getById(id)));
    }
    
    @GetMapping("/filter")
    public Response<PageResponse<TypeResponse>> filterTypes(
            @RequestParam(required = false) String name,
            Pageable pageable) {

        TypeDto dto = TypeDto.builder()
                .name(name)
                .build();
        return respond(toPageResponse(service.filterTypes(dto, pageable)));
    }



    private Page<TypeResponse> toPageResponse(Page<TypeDto> typeDtos) {
        return PageUtil.pageToDto(typeDtos, TypeResponse::toResponse);
    }
}
