package com.example.secured_app.auth;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "change_passwords")
public class ChangePassword {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String email;
    private String newPassword;
    private LocalDateTime submited = LocalDateTime.now();

    public ChangePassword(String email, String newPassword) {
        this.email = email;
        this.newPassword = newPassword;
    }

    public ChangePassword() {
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public boolean expired() {
        return submited.isBefore(LocalDateTime.now().minusMinutes(15));
    }
}
