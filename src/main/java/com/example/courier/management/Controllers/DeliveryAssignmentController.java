package com.example.courier.management.Controllers;

import com.example.courier.management.PayLoads.DeliveryAssignmentRequestDTO;
import com.example.courier.management.PayLoads.DeliveryAssignmentResponseDTO;
import com.example.courier.management.Services.DeliveryAssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
