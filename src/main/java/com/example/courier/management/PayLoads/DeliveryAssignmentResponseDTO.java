package com.example.courier.management.PayLoads;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryAssignmentResponseDTO {

    private Integer assignmentId;
    private Integer packageId;
    private Integer agentId;
    private String status;
    private LocalDateTime assignedAt;

}
