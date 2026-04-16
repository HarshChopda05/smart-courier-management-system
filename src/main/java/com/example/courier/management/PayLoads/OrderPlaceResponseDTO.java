package com.example.courier.management.PayLoads;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderPlaceResponseDTO {

    private Integer orderId;
    private String message;
    private double totalAmount;
    private String orderStatus;
    private LocalDateTime createdAt;

    private String address;
    private String city;
    private Integer pincode;
    private List<PackageResponseDTO> packages;

}
