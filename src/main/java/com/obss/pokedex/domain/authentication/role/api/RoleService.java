package com.obss.pokedex.domain.authentication.role.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    RoleDto createRole(RoleDto dto);

    RoleDto updateRole(String id, RoleDto dto);

    void deleteRole(String id);

    Page<RoleDto> getAllRolePageable(Pageable pageable);

    Page<RoleDto> filterRole(RoleDto dto, Pageable pageable);

    RoleDto getRoleDtoById(String id);

    RoleDto getByRoleName(String roleName);

    List<String> getRolesByRoleName(List<String> roleIds);
}
