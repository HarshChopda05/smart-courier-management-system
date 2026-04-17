package com.example.courier.management.Security;

import com.example.courier.management.Models.User;
import com.example.courier.management.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("User name: {}", username);

        User user = userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User is not Found: "));
        log.debug("Users email: {}", user);
        return user;
    }
}
