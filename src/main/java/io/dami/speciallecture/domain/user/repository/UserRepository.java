package io.dami.speciallecture.domain.user.repository;

import io.dami.speciallecture.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long userId);
}
