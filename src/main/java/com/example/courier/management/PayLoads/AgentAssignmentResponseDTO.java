package com.example.courier.management.PayLoads;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgentAssignmentResponseDTO {

    private Integer assignmentId;
    private LocalDateTime assignedAt;
    private String deliveryStatus;
    private String address;
    private String city;

    private List<PackageResponseDTO> packages;

}
