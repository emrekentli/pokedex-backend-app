package com.obss.pokedex.domain.authentication.user.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDto createUser(UserDto dto);

    UserDto updateUser(String id, UserDto dto);

    void deleteUser(String id);

    Page<UserDto> getAllUserPageable(Pageable pageable);

    Page<UserDto> filterUser(UserDto dto, Pageable pageable);

    UserDto getUserDtoById(String id);

    UserDto getByUserName(String userName);

    UserDto updateMyUser(UserDto dto);
}
