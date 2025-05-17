package com.example.secured_app.auth;

import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;
    private AccountJpaRepository accountJpaRepository;
    private ChangePasswordJpaRepository changePasswordJpaRepository;

    private static final String PASSWORD_REGEX =
            "^(?=.*[a-z])" +          // minimum 1 small letter
                    "(?=.*[A-Z])" +           //minimum 1 big letter
                    "(?=.*\\d)" +             // minimum 1 digit
                    "(?=.*[!@#$%^&*()\\-_=+{};:,<.>])" +  // 1 special sign
                    "[A-Za-z\\d!@#$%^&*()\\-_=+{};:,<.>]{8,20}$"; //allowed signs and length

    public AuthService(PasswordEncoder passwordEncoder, AccountJpaRepository accountJpaRepository, ChangePasswordJpaRepository changePasswordJpaRepository) {
        this.passwordEncoder = passwordEncoder;
        this.accountJpaRepository = accountJpaRepository;
        this.changePasswordJpaRepository = changePasswordJpaRepository;
    }

    void register(Account account) {
        if (accountJpaRepository.existsByEmail(account.getEmail())) {
            throw new IllegalArgumentException("Login już zajęty");
        }
        if (isValidPassword(account.getPassword())) {
            throw new IllegalArgumentException("Hasło nie spełnia wymogów");
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountJpaRepository.save(account);
    }

    @Transactional
    public void changePassword(Account account) {
        //don't check if mail exists for safety reasons
        if (isValidPassword(account.getPassword())) {
            throw new IllegalArgumentException("Hasło nie spełnia wymogów");
        }
        changePasswordJpaRepository.findByEmail(account.getEmail())
                .ifPresent(changePassword -> {
                    if (!changePassword.expired()) {
                        throw new IllegalArgumentException("Już wysłano");
                    } else {
                        changePasswordJpaRepository.delete(changePassword);
                    }
                });
        changePasswordJpaRepository.save(new ChangePassword(account.getEmail(), passwordEncoder.encode(account.getPassword())));
    }

    private boolean isValidPassword(String password) {
        return password != null && password.matches(PASSWORD_REGEX);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountJpaRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));
        return User.withUsername(account.getEmail())
                .password(account.getPassword())
                .roles("USER")
                .build();
    }
}
