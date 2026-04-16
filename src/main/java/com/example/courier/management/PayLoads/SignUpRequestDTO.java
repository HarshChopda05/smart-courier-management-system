package com.example.courier.management.PayLoads;

import com.example.courier.management.Models.Type.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequestDTO {

    @NotEmpty
    @Size(min = 4, message = "Username must be in 4 Characters!")
    private String userName;

    @NotEmpty
    @Size(min = 8, max = 20, message = "Password must be minimum of 8 Characters and maximum of 20 Characters !")
    private String password;

    @NotEmpty
    @Email(message = "Email address is not valid!")
    private String email;

    private Set<RoleType> roles = new HashSet<>();

}
