package com.example.courier.management.PayLoads;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageResponseDTO {

    private Integer packageId;
    private String packageName;
    private Double weight;
    private String packageStatus;
}
