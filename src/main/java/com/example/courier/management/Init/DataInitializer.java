package com.example.courier.management.Init;

import com.example.courier.management.Models.Type.RoleType;
import com.example.courier.management.Models.User;
import com.example.courier.management.Repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdmin() {
        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {

            User admin = User.builder()
                    .userName("Admin")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("admin123"))
                    .roles(Set.of(RoleType.ADMIN))
                    .build();

            log.info("Admin Created: {}", admin);
            userRepository.save(admin);
        }
    }

}
