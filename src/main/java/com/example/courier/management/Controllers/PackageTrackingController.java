package com.example.courier.management.Controllers;

import com.example.courier.management.PayLoads.TrackingResponseDTO;
import com.example.courier.management.Services.PackageTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/courier/customer/tracking")
@RequiredArgsConstructor
public class PackageTrackingController {

    private final PackageTrackingService packageTrackingService;

    @GetMapping("/{packageId}")
    private ResponseEntity<List<TrackingResponseDTO>> trackPackage(@PathVariable Integer packageId, Authentication authentication){
        String email = authentication.getName();
        return new ResponseEntity<>(packageTrackingService.trackPackage(packageId, email), HttpStatus.OK);
    }
}
