package com.ll.exam.sbb.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Integer> {
    boolean existsByUsername(String username);

    Optional<SiteUser> findByUsername(String username);
}
