package com.example.courier.management.PayLoads;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardResponseDTO {

    private long totalPackages;
    private long delivered;
    private long inTransit;
    private long failed;

}
