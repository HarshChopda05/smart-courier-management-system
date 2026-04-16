package com.example.courier.management.PayLoads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlaceRequestDTO {

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotNull(message = "Pincode is required")
    private Integer pincode;

    @NotEmpty(message = "At least one package is required")
    private List<PackageRequestDTO> packages;

}
