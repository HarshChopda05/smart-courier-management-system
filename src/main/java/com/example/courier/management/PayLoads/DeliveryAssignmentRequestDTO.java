package com.example.courier.management.PayLoads;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DeliveryAssignmentRequestDTO {

    @NotNull(message = "Package id is required!")
    private List<Integer> packageIds;

    @NotNull(message = "Agent id is required!")
    private Integer agentId;

}
