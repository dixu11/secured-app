package com.example.secured_app.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChangePasswordJpaRepository extends JpaRepository<ChangePassword,String> {
    Optional<ChangePassword> findByEmail(String email);
}
