package com.example.courier.management.Services;

import com.example.courier.management.PayLoads.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DeliveryAssignmentService {
    List<DeliveryAssignmentResponseDTO> assignPackage(@Valid DeliveryAssignmentRequestDTO requestDTO);
    PageResponseDTO<PackageResponseDTO> getAllPackages(int page, int size, String sortBy, String sortDir);
    PageResponseDTO<DeliveryAssignmentResponseDTO> getAllAssignments(int page, int size, String sortBy, String sortDir);
    PageResponseDTO<AgentResponseDTO> getAllAgents(int page, int size, String sortBy, String sortDir);
    DashboardResponseDTO getDashboard();
}
