package io.dami.speciallecture.infrastructure.core.user;

import io.dami.speciallecture.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {

}
