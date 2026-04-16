package com.example.courier.management.Services.Implementations;

import com.example.courier.management.Exceptions.ResourceNotFoundException;
import com.example.courier.management.Models.Location;
import com.example.courier.management.Models.PackageTracking;
import com.example.courier.management.Models.Package;
import com.example.courier.management.Models.User;
import com.example.courier.management.PayLoads.TrackingResponseDTO;
import com.example.courier.management.Repositories.PackageRepository;
import com.example.courier.management.Repositories.PackageTrackingRepository;
import com.example.courier.management.Repositories.UserRepository;
import com.example.courier.management.Services.PackageTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageTrackingImpl implements PackageTrackingService {

    private final PackageTrackingRepository packageTrackingRepository;
    private final PackageRepository packageRepository;
    private final UserRepository userRepository;


    @Override
    public List<TrackingResponseDTO> trackPackage(Integer packageId, String email) {

        Package pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package Not Found!"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found!"));

        if (!pkg.getOrder().getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalStateException("You are not authorized to track this package");
        }

        List<PackageTracking> trackingList = packageTrackingRepository.findByPkg(pkg);
        if (trackingList.isEmpty()) {
            throw new ResourceNotFoundException("No tracking data found");
        }

        return trackingList.stream()
                .sorted(Comparator.comparing(PackageTracking::getTimeStamp))
                .map(track -> {

                    Location location = track.getLocation();

                    return TrackingResponseDTO.builder()
                            .packageId(pkg.getPackageId()) //
                            .packageName(pkg.getPackageName())
                            .status(track.getPackageStatus().name())
                            .address(location != null ? location.getAddress() : null)
                            .city(location != null ? location.getCity() : null)
                            .timeStamp(track.getTimeStamp())
                            .build();
                })
                .toList();
    }
}
