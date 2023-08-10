package com.obss.pokedex.domain.authentication.role.impl;

import com.obss.pokedex.domain.authentication.role.api.RoleDto;
import com.obss.pokedex.domain.authentication.role.api.RoleService;
import com.obss.pokedex.library.util.PageUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;


    @Override
    public RoleDto createRole(RoleDto dto) {
        Role role = toEntity(new Role(), dto);
        return (repository.save(role).toDto());
    }

    private Role toEntity(Role role, RoleDto dto) {
        role.setName(dto.getName());
        role.setDisplayName(dto.getDisplayName());
        return role;
    }

    @Override
    public RoleDto updateRole(String id, RoleDto dto) {
        return repository.findById(id)
                .map(role -> toEntity(role, dto))
                .map(repository::save)
                .map(Role::toDto)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void deleteRole(String id) {
        repository.delete(repository.findById(id).orElseThrow(EntityNotFoundException::new));
    }


    @Override
    public Page<RoleDto> getAllRolePageable(Pageable pageable) {
        return PageUtil.pageToDto(repository.findAll(pageable), Role::toDto);
    }

    @Override
    public Page<RoleDto> filterRole(RoleDto dto, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
        return PageUtil.pageToDto(repository.findAll(Example.of(toEntity(new Role(), dto),matcher), pageable), Role::toDto);
    }

    @Override
    public RoleDto getRoleDtoById(String id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new).toDto();
    }

    @Override
    public RoleDto getByRoleName(String roleName) {
        return repository.findByName(roleName).orElseThrow(EntityNotFoundException::new).toDto();
    }

    @Override
    public List<String> getRolesByRoleName(List<String> roleIds) {
        return repository.findByIdIn(roleIds).stream().map(Role::getName).toList();
    }

    public Set<Role>  getRolesByRoleNames(Set<String> roleNames) {
        return repository.findByNameIn(roleNames);
    }

    public Set<Role> getByRoleEntityByName(String roleUser) {
        return Set.of(repository.findByName(roleUser).orElseThrow(EntityNotFoundException::new));
    }
    public Role findRoleById(String roleUser) {
       return repository.findById(roleUser).orElseThrow(EntityNotFoundException::new);
    }
}
