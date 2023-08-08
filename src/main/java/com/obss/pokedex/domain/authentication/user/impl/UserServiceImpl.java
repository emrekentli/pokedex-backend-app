package com.obss.pokedex.domain.authentication.user.impl;

import com.obss.pokedex.domain.authentication.role.api.RoleDto;
import com.obss.pokedex.domain.authentication.role.impl.Role;
import com.obss.pokedex.domain.authentication.role.impl.RoleServiceImpl;
import com.obss.pokedex.domain.authentication.user.api.UserDto;
import com.obss.pokedex.domain.authentication.user.api.UserRetrievalService;
import com.obss.pokedex.domain.authentication.user.api.UserService;
import com.obss.pokedex.domain.pokemon.pokemon.api.PokemonDto;
import com.obss.pokedex.domain.pokemon.pokemon.api.PokemonService;
import com.obss.pokedex.library.util.PageUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final RoleServiceImpl roleService;
    private final PokemonService pokemonService;
    private final UserRetrievalService userRetrievalService;
    @Override
    @Transactional
    public UserDto createUser(UserDto dto) {
        User user = toEntity(new User(), dto);
        return toDto(repository.save(user));
    }

    @Override
    @Transactional
    public UserDto updateUser(String id, UserDto dto) {
        return repository.findById(id)
                .map(user -> toEntity(user, dto))
                .map(repository::save)
                .map(this::toDto)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @Transactional
    public void deleteUser(String id) {
        repository.delete(repository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public Page<UserDto> getAllUserPageable(Pageable pageable) {
        return PageUtil.pageToDto(repository.findAll(pageable), this::toDto);
    }

    @Override
    public Page<UserDto> filterUser(UserDto dto, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
        return PageUtil.pageToDto(repository.findAll(Example.of(toEntity(new User(), dto),matcher), pageable), this::toDto);
    }

    public User getUserById(String id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public UserDto getUserDtoById(String id) {
        return toDto(repository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found")));
    }

    @Override
    public UserDto getByUserName(String userName) {
        return repository.findByUserName(userName)
                .map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
    }

    @Override
    public UserDto updateMyUser(UserDto dto) {
      return repository.findById(userRetrievalService.getCurrentUserId())
                .map(user -> toEntity(user, dto))
                .map(repository::save)
                .map(this::toDto)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public UserDto addPokemonToCatchlist( String pokemonId) {
        PokemonDto pokemon = pokemonService.getById(pokemonId);
        return repository.findById(userRetrievalService.getCurrentUserId())
                .map(user -> {
                    user.getCatchList().add(pokemon.getId());
                    return repository.save(user);
                })
                .map(this::toDto)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public UserDto addPokemonToWishlist(String pokemonId) {
        PokemonDto pokemon = pokemonService.getById(pokemonId);
        return repository.findById(userRetrievalService.getCurrentUserId())
                .map(user -> {
                    user.getWishList().add(pokemon.getId());
                    return repository.save(user);
                })
                .map(this::toDto)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public UserDto deletePokemonFromCatchlist(String pokemonId) {
        PokemonDto pokemon = pokemonService.getById(pokemonId);
        return repository.findById(userRetrievalService.getCurrentUserId())
                .map(user -> {
                    user.getCatchList().remove(pokemon.getId());
                    return repository.save(user);
                })
                .map(this::toDto)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public UserDto deletePokemonFromWishlist(String pokemonId) {
        PokemonDto pokemon = pokemonService.getById(pokemonId);
        return repository.findById(userRetrievalService.getCurrentUserId())
                .map(user -> {
                    user.getWishList().remove(pokemon.getId());
                    return repository.save(user);
                })
                .map(this::toDto)
                .orElseThrow(EntityNotFoundException::new);
    }


    public User getUserByUserName(String username) {
        return repository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("Kayıt bulunamadı"));
    }

    public void saveUser(User user) {
        repository.save(user);
    }

    public User toEntity(User user, UserDto dto) {
        user.setUserName(dto.getUserName());
        user.setPassword(dto.getPassword());
        user.setActivity(dto.getActivity());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRoles(dto.getRoles() != null ? roleService.getRolesByRoleNames(dto.getRoles().stream().map(RoleDto::getName).collect(Collectors.toSet())) : null);
        return user;
    }

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .created(user.getCreated())
                .modified(user.getModified())
                .userName(user.getUserName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .activity(user.getActivity())
                .roles(user.getRoles() != null ? user.getRoles().stream().map(Role::toDto).collect(Collectors.toSet()) : null)
                .fullName(user.getFullName())
                .build();
    }


    public void checkUserExists(String userName) {
        repository.findByUserName(userName)
                .ifPresent(u -> {
                    throw new IllegalArgumentException("User with userName " + userName + " already exist");
                });
    }
}
