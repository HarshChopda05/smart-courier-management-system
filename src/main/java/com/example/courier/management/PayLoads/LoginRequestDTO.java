package com.example.courier.management.PayLoads;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @NotBlank(message = "Email is Required!")
    private String email;

    @NotBlank(message = "Message is required!")
    private String password;

}
