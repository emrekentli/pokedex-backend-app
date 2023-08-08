package com.obss.pokedex.domain.authentication.user.impl;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByUserName(String userName);

    Optional<User> findUserByUserName(String username);
}
