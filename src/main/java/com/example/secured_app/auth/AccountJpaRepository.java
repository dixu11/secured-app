package com.example.secured_app.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByLogin(String login);

    boolean existsByLogin(String login);
}
