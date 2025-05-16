package com.example.secured_app.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private PasswordEncoder passwordEncoder;
    private AccountJpaRepository accountJpaRepository;

    private static final String PASSWORD_REGEX =
            "^(?=.*[a-z])" +          // co najmniej 1 mała litera
                    "(?=.*[A-Z])" +           // co najmniej 1 wielka litera
                    "(?=.*\\d)" +             // co najmniej 1 cyfra
                    "(?=.*[!@#$%^&*()\\-_=+{};:,<.>])" +  // co najmniej 1 znak specjalny
                    "[A-Za-z\\d!@#$%^&*()\\-_=+{};:,<.>]{8,20}$"; // dozwolone znaki + długość

    public AuthService(PasswordEncoder passwordEncoder, AccountJpaRepository accountJpaRepository) {
        this.passwordEncoder = passwordEncoder;
        this.accountJpaRepository = accountJpaRepository;
    }

    void register(Account account){
        if (accountJpaRepository.existsByLogin(account.getLogin())) {
            throw new IllegalArgumentException("Login już zajęty");
        }
        if (!isValidPassword(account.getPassword())) {
            throw new IllegalArgumentException("Hasło nie spełnia wymogów");
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountJpaRepository.save(account);
    }

    private boolean isValidPassword(String password) {
        return password != null && password.matches(PASSWORD_REGEX);
    }
}
