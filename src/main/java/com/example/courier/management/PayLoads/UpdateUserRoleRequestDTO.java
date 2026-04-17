package com.example.courier.management.PayLoads;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRoleRequestDTO {

    private Set<String> roles;
}
