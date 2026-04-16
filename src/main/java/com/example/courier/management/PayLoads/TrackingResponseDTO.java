package com.example.courier.management.PayLoads;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrackingResponseDTO {

    private Integer packageId;
    private String packageName;
    private String status;
    private String address;
    private String city;
    private LocalDateTime timeStamp;

}
