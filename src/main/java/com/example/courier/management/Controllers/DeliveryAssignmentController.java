package com.example.courier.management.Controllers;

import com.example.courier.management.PayLoads.*;
import com.example.courier.management.Services.DeliveryAssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courier/manager")
@RequiredArgsConstructor
public class DeliveryAssignmentController {

    private final DeliveryAssignmentService deliveryAssignmentService;

    @PostMapping("/assign")
    public ResponseEntity<List<DeliveryAssignmentResponseDTO>> assignPackage(@Valid @RequestBody DeliveryAssignmentRequestDTO requestDTO){
        return new ResponseEntity<>(deliveryAssignmentService.assignPackage(requestDTO),HttpStatus.CREATED);
    }

    @GetMapping("/all-packages")
    public ResponseEntity<PageResponseDTO<PackageResponseDTO>> getAllPackages(
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "packageId") String sortBy,
                                @RequestParam(defaultValue = "asc") String sortDir) {

        return new ResponseEntity<>(deliveryAssignmentService.getAllPackages(page, size, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/all-assignments")
    public ResponseEntity<PageResponseDTO<DeliveryAssignmentResponseDTO>> getAllAssignments(
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "assignedId") String sortBy,
                                @RequestParam(defaultValue = "asc") String sortDir) {

        return new ResponseEntity<>(deliveryAssignmentService.getAllAssignments(page, size, sortBy, sortDir), HttpStatus.OK);
    }

    @GetMapping("/all-agents")
    public ResponseEntity<PageResponseDTO<AgentResponseDTO>> getAllAgents(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "userId") String sortBy,
                        @RequestParam(defaultValue = "asc") String sortDir) {

        return new ResponseEntity<>(deliveryAssignmentService.getAllAgents(page, size, sortBy, sortDir), HttpStatus.OK);

    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponseDTO> getDashboard(){
        return new ResponseEntity<>(deliveryAssignmentService.getDashboard(), HttpStatus.OK);
    }
}
