package com.example.courier.management.Services.Implementations;

import com.example.courier.management.Exceptions.UserAlreadyExistsException;
import com.example.courier.management.Models.Type.RoleType;
import com.example.courier.management.Models.User;
import com.example.courier.management.PayLoads.LoginRequestDTO;
import com.example.courier.management.PayLoads.LoginResponseDTO;
import com.example.courier.management.PayLoads.SignUpRequestDTO;
import com.example.courier.management.PayLoads.SignUpResponseDTO;
import com.example.courier.management.Repositories.UserRepository;
import com.example.courier.management.Security.JwtHelper;
import com.example.courier.management.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtHelper jwtHelper;
    private final AuthenticationManager authenticationManager;

    @Override
    public SignUpResponseDTO registerUser(SignUpRequestDTO signUpRequestDTO) {

        if (userRepository.findByEmail(signUpRequestDTO.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("User already exists!");
        }

        User user = modelMapper.map(signUpRequestDTO, User.class);
        user.setPassword(passwordEncoder.encode(signUpRequestDTO.getPassword()));

        //HANDLE ROLES (IMPORTANT)
        if (signUpRequestDTO.getRoles() != null && !signUpRequestDTO.getRoles().isEmpty()) {
            user.setRoles(signUpRequestDTO.getRoles()); //roles from request
        } else {
            user.getRoles().add(RoleType.CUSTOMER); //Default role
        }
        user = userRepository.save(user);

        return new SignUpResponseDTO(user.getUserId(), user.getUsername());
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword()
                )
        );
        //Get user Email
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDTO.getEmail());

        //Generate Token
        String token = this.jwtHelper.generateToken(userDetails.getUsername());

       // LoginResponseDTO responseDTO =LoginResponseDTO.builder().token(token).build();
        return LoginResponseDTO.builder()
                .token(token)
                .build();
    }
}
