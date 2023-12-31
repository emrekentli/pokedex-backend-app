package com.obss.pokedex.domain.authentication.user.impl;

import com.obss.pokedex.domain.authentication.role.api.RoleDto;
import com.obss.pokedex.domain.authentication.role.impl.Role;
import com.obss.pokedex.domain.authentication.role.impl.RoleServiceImpl;
import com.obss.pokedex.domain.authentication.user.api.UserDto;
import com.obss.pokedex.domain.authentication.user.api.UserRetrievalService;
import com.obss.pokedex.domain.authentication.user.api.UserRoleDto;
import com.obss.pokedex.domain.authentication.user.api.UserService;
import com.obss.pokedex.domain.email.api.EmailDto;
import com.obss.pokedex.domain.email.api.EmailService;
import com.obss.pokedex.domain.pokemon.pokemon.api.PokemonDto;
import com.obss.pokedex.domain.pokemon.pokemon.api.PokemonService;
import com.obss.pokedex.library.util.PageUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final RoleServiceImpl roleService;
    private final PokemonService pokemonService;
    private final UserRetrievalService userRetrievalService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    @Override
    @Transactional
    public UserDto createUser(UserDto dto) {
        checkUserExists(dto.getEmail());
        String randomPassword =  randomPassword();
        User user = toEntity(new User(), randomPassword, dto);
        emailService.sendEmail(EmailDto.builder()
                .to(user.getEmail())
                .subject("Welcome to Pokedex")
                .text("Welcome to Pokedex!\n Your password is " + randomPassword)
                .build());
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
        return repository.filterUser(dto.getUserName(), dto.getFullName(), dto.getEmail(), dto.getPhoneNumber(), pageable)
                .map(this::toDto);
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
        checkIfPokemonExistsInCatchlist(pokemon);
        return repository.findById(userRetrievalService.getCurrentUserId())
                .map(user -> {
                    user.getCatchList().add(pokemon.getId());
                    return repository.save(user);
                })
                .map(this::toDto)
                .orElseThrow(EntityNotFoundException::new);
    }

    private void checkIfPokemonExistsInCatchlist(PokemonDto pokemon) {
        if (Boolean.TRUE.equals(repository.findById(userRetrievalService.getCurrentUserId())
                .map(user -> user.getCatchList().contains(pokemon.getId()))
                .orElseThrow(EntityNotFoundException::new))) {
            throw new IllegalArgumentException("Pokemon already exists in catchlist");
        }
    }
    private void checkIfPokemonExistsInWishlist(PokemonDto pokemon) {
        if (Boolean.TRUE.equals(repository.findById(userRetrievalService.getCurrentUserId())
                .map(user -> user.getWishList().contains(pokemon.getId()))
                .orElseThrow(EntityNotFoundException::new))) {
            throw new IllegalArgumentException("Pokemon already exists in wishlist");
        }
    }

    @Override
    public UserDto addPokemonToWishlist(String pokemonId) {
        PokemonDto pokemon = pokemonService.getById(pokemonId);
        checkIfPokemonExistsInWishlist(pokemon);
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

    @Override
    public UserDto addRoleToUser(UserRoleDto dto) {
        var user = repository.findById(dto.getUserId()).orElseThrow(EntityNotFoundException::new);
        var role = roleService.findRoleById(dto.getRoleId());
        checkUserHasRole(user, role);
        user.getRoles().add(role);
        return toDto(repository.save(user));
    }

    @Override
    public UserDto removeRoleToUser(UserRoleDto dto) {
        var user = repository.findById(dto.getUserId()).orElseThrow(EntityNotFoundException::new);
        var role = roleService.findRoleById(dto.getRoleId());
        if(role.getName().equals("ROLE_ADMIN")){
            throw new IllegalArgumentException("Admin role cannot be removed");
        }
        user.getRoles().remove(role);
        return toDto(repository.save(user));
    }

    private void checkUserHasRole(User user, Role role) {
        if (user.getRoles().contains(role)) {
            throw new IllegalArgumentException("User has this role");
        }
    }

    public User getUserByUserName(String username) {
        return repository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("Kayıt bulunamadı"));
    }

    public void saveUser(User user) {
        repository.save(user);
    }

    public User toEntity(User user, UserDto dto) {
        user.setUserName(dto.getUserName());
        user.setPassword(passwordEncoder.encode(randomPassword()));
        user.setActivity(true);
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRoles(dto.getRoles() != null ? roleService.getRolesByRoleNames(dto.getRoles().stream().map(RoleDto::getName).collect(Collectors.toSet())) : null);
        return user;
    }
    public User toEntity(User user, String password, UserDto dto) {
        user.setUserName(dto.getUserName());
        user.setPassword(passwordEncoder.encode(password));
        user.setActivity(true);
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRoles(dto.getRoles() != null ? roleService.getRolesByRoleNames(dto.getRoles().stream().map(RoleDto::getName).collect(Collectors.toSet())) : roleService.getByRoleEntityByName("ROLE_USER"));
        return user;
    }

    private String randomPassword() {
        int randomPIN = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(randomPIN);
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
                .catchList(user.getCatchList() != null ? user.getCatchList() : null)
                .wishList(user.getWishList() != null ? user.getWishList() : null)
                .build();
    }


    public void checkUserExists(String userName) {
        repository.findByEmail(userName)
                .ifPresent(u -> {
                    throw new IllegalArgumentException("User with userName " + userName + " already exist");
                });
    }
}
