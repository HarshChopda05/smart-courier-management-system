package com.example.courier.management.Controllers;

import com.example.courier.management.PayLoads.AgentAssignmentResponseDTO;
import com.example.courier.management.PayLoads.UpdateDeliveryStatusRequestDTO;
import com.example.courier.management.PayLoads.UpdateDeliveryStatusResponseDTO;
import com.example.courier.management.Services.AgentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courier/agent")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    @GetMapping("/packages")
    public ResponseEntity<List<AgentAssignmentResponseDTO>> getAssignedPackages(Authentication authentication){
        String email = authentication.getName();
        return new ResponseEntity<>(agentService.getAssignedPackages(email), HttpStatus.OK);
    }

    @PostMapping("/update-status")
    public ResponseEntity<UpdateDeliveryStatusResponseDTO> updateDeliveryStatus(
            @Valid @RequestBody UpdateDeliveryStatusRequestDTO requestDTO, Authentication authentication){

        String email = authentication.getName();
        return new ResponseEntity<>(agentService.updateDeliveryStatus(email, requestDTO), HttpStatus.CREATED);

    }
}
