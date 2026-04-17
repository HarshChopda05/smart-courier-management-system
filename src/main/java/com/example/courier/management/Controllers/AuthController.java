package com.example.courier.management.Controllers;

import com.example.courier.management.PayLoads.LoginRequestDTO;
import com.example.courier.management.PayLoads.LoginResponseDTO;
import com.example.courier.management.PayLoads.SignUpRequestDTO;
import com.example.courier.management.PayLoads.SignUpResponseDTO;
import com.example.courier.management.Security.JwtHelper;
import com.example.courier.management.Services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("courier/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    private ResponseEntity<SignUpResponseDTO> signUp(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO){
        return new ResponseEntity<>(userService.registerUser(signUpRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    private ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO){

       return new ResponseEntity<>(userService.login(loginRequestDTO), HttpStatus.OK);
    }

}
