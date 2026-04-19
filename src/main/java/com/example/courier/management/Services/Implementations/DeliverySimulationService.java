package com.example.courier.management.Services.Implementations;

import com.example.courier.management.Models.DeliveryAssignment;
import com.example.courier.management.Models.Order;
import com.example.courier.management.Models.Package;
import com.example.courier.management.Models.PackageTracking;
import com.example.courier.management.Models.Type.DeliveryAssignmentStatus;
import com.example.courier.management.Models.Type.OrderStatus;
import com.example.courier.management.Models.Type.PackageStatus;
import com.example.courier.management.Repositories.DeliveryAssignmentRepository;
import com.example.courier.management.Repositories.OrderRepository;
import com.example.courier.management.Repositories.PackageRepository;
import com.example.courier.management.Repositories.PackageTrackingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliverySimulationService {

    private final PackageRepository packageRepository;
    private final PackageTrackingRepository packageTrackingRepository;
    private final DeliveryAssignmentRepository deliveryAssignmentRepository;
    private final OrderRepository orderRepository;

    @Scheduled(fixedRate = 30000) //every 1 minute run this method
    @Transactional
    public void simulateDelivery(){

        log.debug("Scheduler simulation Running...");

        List<Package> packages = packageRepository
                .findActivePackagesWithOrder(
                        List.of(PackageStatus.DELIVERED, PackageStatus.FAILED)
                );

        for (Package pkg : packages) {

            //Skip if no assignment
            DeliveryAssignment assignment =
                    deliveryAssignmentRepository.findByPkgs(pkg).stream().findFirst().orElse(null);

            if (assignment == null) {
                continue;
            }

            //Skip if not actively assigned
            if (!(assignment.getDeliveryAssignmentStatus() == DeliveryAssignmentStatus.ASSIGNED ||
                    assignment.getDeliveryAssignmentStatus() == DeliveryAssignmentStatus.IN_PROGRESS)) {
                continue;
            }
//            //Skip if not actively assigned
//            if (!(assignment.getDeliveryAssignmentStatus() == DeliveryAssignmentStatus.DELIVERED ||
//                    assignment.getDeliveryAssignmentStatus() == DeliveryAssignmentStatus.FAILED)) {
//                continue;
//            }

            PackageStatus current = pkg.getPackageStatus();

            log.debug("Current Status: {}", current);
            PackageStatus next = getNextStatus(current);
            log.debug("Updated Status: {}", next);

            if (next == null || next == current) continue;

           // pkg.setPackageStatus(next);

            // UPDATE PACKAGE (MISSING IN YOUR CODE)
            pkg.setPackageStatus(next);

            packageTrackingRepository.save(PackageTracking.builder()
                    .pkg(pkg)
                    .packageStatus(next)
                    .timeStamp(LocalDateTime.now())
                    .location(pkg.getOrder().getLocation())
                    .build());

            //Assignment update
            updateAssignment(assignment, next);


            //Update Order Status
            Order order = pkg.getOrder();

            // OrderStatus orderStatus = mapToOrderStatus(next);
            order.setOrderStatus(calculateOrderStatus(order));
            order.setUpdatedAt(LocalDateTime.now());
        }

            packageRepository.saveAll(packages);

    }
    private void updateAssignment(DeliveryAssignment assignment, PackageStatus next) {

        if (next == PackageStatus.PICKED) {
            assignment.setPickedAt(LocalDateTime.now());
            assignment.setDeliveryAssignmentStatus(DeliveryAssignmentStatus.IN_PROGRESS);

        } else if (next == PackageStatus.IN_TRANSIT) {
            assignment.setDeliveryAssignmentStatus(DeliveryAssignmentStatus.IN_PROGRESS);

        } else if (next == PackageStatus.OUT_FOR_DELIVERY) {
            assignment.setDeliveryAssignmentStatus(DeliveryAssignmentStatus.IN_PROGRESS);

        } else if (next == PackageStatus.DELIVERED) {
            assignment.setDeliveredAt(LocalDateTime.now());
            assignment.setDeliveryAssignmentStatus(DeliveryAssignmentStatus.DELIVERED);

        } else if (next == PackageStatus.FAILED) {
            assignment.setDeliveryAssignmentStatus(DeliveryAssignmentStatus.FAILED);
        }
        log.debug("Delivery Assignment Status Updated: {}", assignment);
    }

    private OrderStatus calculateOrderStatus(Order order) {

        List<PackageStatus> statuses = order.getPackages()
                .stream()
                .map(Package::getPackageStatus)
                .toList();

        if (statuses.stream().allMatch(s -> s == PackageStatus.DELIVERED)) {
            return OrderStatus.DELIVERED;
        }

        if (statuses.stream().anyMatch(s -> s == PackageStatus.FAILED)) {
            return OrderStatus.FAILED;
        }

        if (statuses.stream().anyMatch(s ->
                s == PackageStatus.PICKED ||
                        s == PackageStatus.IN_TRANSIT ||
                        s == PackageStatus.OUT_FOR_DELIVERY)) {
            return OrderStatus.SHIPPED;
        }

        return OrderStatus.CREATED;
    }

    private PackageStatus getNextStatus(PackageStatus current) {
        return switch (current) {
            case CREATED -> PackageStatus.PICKED;
            case PICKED -> PackageStatus.IN_TRANSIT;
            case IN_TRANSIT -> PackageStatus.OUT_FOR_DELIVERY;
            case OUT_FOR_DELIVERY -> PackageStatus.DELIVERED;
            default -> null;
        };
    }
}