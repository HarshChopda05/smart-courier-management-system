package com.example.courier.management.PayLoads;

import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {

    private Integer userId;
    private String userName;
    private String email;
    private Set<String> roles;
}
