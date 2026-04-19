package com.example.courier.management.Services;

import com.example.courier.management.PayLoads.LoginRequestDTO;
import com.example.courier.management.PayLoads.LoginResponseDTO;
import com.example.courier.management.PayLoads.SignUpRequestDTO;
import com.example.courier.management.PayLoads.SignUpResponseDTO;
import jakarta.validation.Valid;



public interface UserService {


    SignUpResponseDTO registerUser(@Valid SignUpRequestDTO signUpRequestDTO);

    LoginResponseDTO login(@Valid LoginRequestDTO loginRequestDTO);
}
