package com.example.courier.management.PayLoads;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateDeliveryStatusResponseDTO {

    private Integer packageId;
    private String status;
    private LocalDateTime updatedAt;

}
