package com.example.courier.management.Services.Implementations;

import com.example.courier.management.Models.Package;
import com.example.courier.management.Models.PackageTracking;
import com.example.courier.management.Models.Type.PackageStatus;
import com.example.courier.management.Repositories.PackageRepository;
import com.example.courier.management.Repositories.PackageTrackingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliverySimulationService {

    private final PackageRepository packageRepository;
    private final PackageTrackingRepository packageTrackingRepository;

    @Scheduled(fixedRate = 60000) //every 1 minute run this method
    public void simulateDelivery(){

        List<Package> packages = packageRepository
                .findActivePackagesWithOrder(
                        List.of(PackageStatus.DELIVERED, PackageStatus.FAILED)
                );
        log.info("Running delivery simulation...");

        for (Package pkg : packages){
            PackageStatus current = pkg.getPackageStatus();

            if (current == PackageStatus.DELIVERED || current == PackageStatus.FAILED){
                continue;
            }

            PackageStatus next = getNextStatus(current);

            if (next != null && next != current){
                pkg.setPackageStatus(next);

                PackageTracking tracking = PackageTracking.builder()
                        .pkg(pkg)
                        .packageStatus(next)
                        .timeStamp(LocalDateTime.now())
                        .location(pkg.getOrder().getLocation())
                        .build();

                packageTrackingRepository.save(tracking);
            }
        }
        packageRepository.saveAll(packages);
    }

    private PackageStatus getNextStatus(PackageStatus current){

        return switch (current){
            case CREATED -> PackageStatus.PICKED;
            case PICKED -> PackageStatus.IN_TRANSIT;
            case IN_TRANSIT -> PackageStatus.OUT_FOR_DELIVERY;
            case OUT_FOR_DELIVERY ->  PackageStatus.DELIVERED;
            default -> null;
        };
    }
}
