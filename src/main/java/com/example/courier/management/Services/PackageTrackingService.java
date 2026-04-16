package com.example.courier.management.Services;

import com.example.courier.management.PayLoads.TrackingResponseDTO;

import java.util.List;

public interface PackageTrackingService {
    List<TrackingResponseDTO> trackPackage(Integer packageId, String email);

}
