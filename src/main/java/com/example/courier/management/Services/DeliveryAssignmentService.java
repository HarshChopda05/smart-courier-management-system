package com.example.courier.management.Services;

import com.example.courier.management.PayLoads.DeliveryAssignmentRequestDTO;
import com.example.courier.management.PayLoads.DeliveryAssignmentResponseDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface DeliveryAssignmentService {
    List<DeliveryAssignmentResponseDTO> assignPackage(@Valid DeliveryAssignmentRequestDTO requestDTO);
}
