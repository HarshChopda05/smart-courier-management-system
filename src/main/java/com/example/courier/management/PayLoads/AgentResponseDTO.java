package com.example.courier.management.PayLoads;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgentResponseDTO {

    private Integer agentId;
    private String email;

}
