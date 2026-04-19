package com.example.courier.management.Services;

import com.example.courier.management.PayLoads.AgentAssignmentResponseDTO;
import com.example.courier.management.PayLoads.UpdateDeliveryStatusRequestDTO;
import com.example.courier.management.PayLoads.UpdateDeliveryStatusResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatusCode;

import java.util.List;

public interface AgentService {
    List<AgentAssignmentResponseDTO> getAssignedPackages(String email);
    UpdateDeliveryStatusResponseDTO updateDeliveryStatus(String email, @Valid UpdateDeliveryStatusRequestDTO requestDTO);
}
