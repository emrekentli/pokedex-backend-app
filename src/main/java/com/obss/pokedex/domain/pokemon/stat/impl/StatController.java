package com.obss.pokedex.domain.pokemon.stat.impl;

import com.obss.pokedex.domain.pokemon.stat.api.StatDto;
import com.obss.pokedex.domain.pokemon.stat.api.StatService;
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
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatController extends BaseController {
    private final StatService service;
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public Response<StatResponse> createStat(@Valid @RequestBody StatRequest request) {
        var stat = service.createStat(request.toDto());
        return respond(StatResponse.toResponse(stat));
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public Response<StatResponse> updateStat(@Valid @PathVariable(name = "id") String id,
                                             @RequestBody StatRequest request) {
        var stat = service.updateStat(id, request.toDto());
        return respond(StatResponse.toResponse(stat));
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public Response<Void> deleteStat(@PathVariable(name = "id") String id) {
        service.deleteStat(id);
        return new Response<>(MetaResponse.ofSuccess());
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public Response<PageResponse<StatResponse>> getAll(Pageable pageable) {
        return respond(toPageResponse(service.getAll(pageable)));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public Response<StatResponse> getById(@PathVariable(value = "id") String id) {
        return respond(StatResponse.toResponse(service.getById(id)));
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/filter")
    public Response<PageResponse<StatResponse>> filterStats(
            @RequestParam(required = false) String name,
            Pageable pageable) {

        StatDto dto = StatDto.builder()
                .name(name)
                .build();
        return respond(toPageResponse(service.filterStats(dto, pageable)));
    }



    private Page<StatResponse> toPageResponse(Page<StatDto> statDtos) {
        return PageUtil.pageToDto(statDtos, StatResponse::toResponse);
    }
}
