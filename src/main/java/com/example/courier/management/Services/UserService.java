package com.example.courier.management.Services;

import com.example.courier.management.PayLoads.RequestDTO.LoginRequestDTO;
import com.example.courier.management.PayLoads.ResponseDTO.LoginResponseDTO;
import com.example.courier.management.PayLoads.RequestDTO.SignUpRequestDTO;
import com.example.courier.management.PayLoads.ResponseDTO.SignUpResponseDTO;
import jakarta.validation.Valid;



public interface UserService {


    SignUpResponseDTO registerUser(@Valid SignUpRequestDTO signUpRequestDTO);

    LoginResponseDTO login(@Valid LoginRequestDTO loginRequestDTO);
}
