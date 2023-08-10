package com.obss.pokedex.domain.authentication.role.impl;

import com.obss.pokedex.domain.authentication.role.api.RoleDto;
import com.obss.pokedex.domain.authentication.role.api.RoleService;
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
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController extends BaseController {
    private final RoleService service;
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @PostMapping
    public Response<RoleResponse> createRole(@Valid @RequestBody RoleRequest request) {
        var role = service.createRole(request.toDto());
        return respond(RoleResponse.toResponse(role));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @PutMapping("/{id}")
    public Response<RoleResponse> updateRole(@Valid @PathVariable(name = "id") String id,
                                             @RequestBody RoleRequest request) {
        var role = service.updateRole(id, request.toDto());
        return respond(RoleResponse.toResponse(role));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @DeleteMapping("/{id}")
    public Response<Void> deleteRole(@PathVariable(name = "id") String id) {
        service.deleteRole(id);
        return new Response<>(MetaResponse.ofSuccess());
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @GetMapping
    public Response<PageResponse<RoleResponse>> getAllRoles(Pageable pageable) {
        return respond(toPageResponse(service.getAllRolePageable(pageable)));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @GetMapping("/filter")
    public Response<PageResponse<RoleResponse>> filterUser(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String displayName,
            Pageable pageable) {

        RoleDto dto = RoleDto.builder()
                .name(name)
                .displayName(displayName)
                .build();
        return respond(toPageResponse(service.filterRole(dto, pageable)));
    }

    private Page<RoleResponse> toPageResponse(Page<RoleDto> roles) {
        return PageUtil.pageToDto(roles, RoleResponse::toResponse);
    }
}
