package io.github.dohyeon0608.web.reservation.repository;

import io.github.dohyeon0608.web.reservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail(String email);
}
