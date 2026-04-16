package com.example.courier.management.PayLoads;

import com.example.courier.management.Models.Type.PackageStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateDeliveryStatusRequestDTO {

    @NotNull(message = "package id is required!")
    private Integer packageId;

    @NotNull(message = "status is required!")
    @Enumerated(EnumType.STRING)
    private PackageStatus status;
}
