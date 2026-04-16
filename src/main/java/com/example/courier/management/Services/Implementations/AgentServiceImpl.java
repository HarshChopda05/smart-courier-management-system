package com.example.courier.management.Services.Implementations;

import com.example.courier.management.Exceptions.ResourceNotFoundException;
import com.example.courier.management.Models.*;
import com.example.courier.management.Models.Package;
import com.example.courier.management.Models.Type.DeliveryAssignmentStatus;
import com.example.courier.management.Models.Type.OrderStatus;
import com.example.courier.management.Models.Type.PackageStatus;
import com.example.courier.management.PayLoads.AgentAssignmentResponseDTO;
import com.example.courier.management.PayLoads.PackageResponseDTO;
import com.example.courier.management.PayLoads.UpdateDeliveryStatusRequestDTO;
import com.example.courier.management.PayLoads.UpdateDeliveryStatusResponseDTO;
import com.example.courier.management.Repositories.*;
import com.example.courier.management.Services.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {

    private final UserRepository userRepository;
    private final PackageRepository packageRepository;
    private final DeliveryAssignmentRepository deliveryAssignmentRepository;
    private final PackageTrackingRepository packageTrackingRepository;
    private final OrderRepository orderRepository;


    @Override
    public List<AgentAssignmentResponseDTO> getAssignedPackages(String email) {

        User agent =userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Agent Not Found!"));

        List<DeliveryAssignment> assignments = deliveryAssignmentRepository.findByAgent(agent);

        if (assignments.isEmpty()) {
            throw new RuntimeException("No packages assigned to this agent");
        }

        return assignments.stream().map(assignment ->{

            Package pkg = assignment.getPkg();

            PackageResponseDTO packageDTO = PackageResponseDTO.builder()
                    .packageId(pkg.getPackageId())
                    .packageName(pkg.getPackageName())
                    .weight(pkg.getWeight())
                    .packageStatus(pkg.getPackageStatus().name())
                    .build();

            Location location = pkg.getOrder().getLocation();

            return AgentAssignmentResponseDTO.builder()
                    .assignmentId(assignment.getAssignedId())
                    .assignedAt(assignment.getAssignedAt())
                    .deliveryStatus(assignment.getDeliveryAssignmentStatus().name())
                    .address(location.getAddress())
                    .city(location.getCity())
                    .packages(List.of(packageDTO))
                    .build();
        })
                .toList();

    }

    /*
Correct Mapping (REAL WORLD)
PackageStatus--->DeliveryAssignmentStatus
PICKED-->       IN_PROGRESS
IN_TRANSIT-->	IN_PROGRESS
DELI    VERED-->	DELIVERED
FAILED-->   	FAILED

Package Delivery Status flow = CREATED → PICKED → IN_TRANSIT → OUT_FOR_DELIVERY → (DELIVERED or FAILED)
*/

    @Override
    public UpdateDeliveryStatusResponseDTO updateDeliveryStatus(String email, UpdateDeliveryStatusRequestDTO requestDTO) {

        LocalDateTime now = LocalDateTime.now();

        User agent = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Agent Not Found!"));

        Package pkg = packageRepository.findById(requestDTO.getPackageId()).orElseThrow(()->
                new ResourceNotFoundException("Package Not Found!"));


                //Check Valid Assignment
        DeliveryAssignment assignment = deliveryAssignmentRepository
                .findByPkgAndAgent(pkg, agent)
                .orElseThrow(()-> new IllegalStateException("This package is not assigned to you!"));

        PackageStatus currentStatus = pkg.getPackageStatus();
        PackageStatus newStatus = requestDTO.getStatus();

        if (currentStatus == PackageStatus.DELIVERED) {
            throw new IllegalStateException("Package already delivered");
        }

        if (currentStatus == newStatus) {
            throw new IllegalStateException("Already in same status");
        }

        // enforce flow
        if (currentStatus == PackageStatus.CREATED && newStatus != PackageStatus.PICKED) {
            throw new IllegalStateException("Must PICKED first");
        }

        if (currentStatus == PackageStatus.PICKED && newStatus != PackageStatus.IN_TRANSIT) {
            throw new IllegalStateException("Must move to IN_TRANSIT");
        }

        if (currentStatus == PackageStatus.IN_TRANSIT && newStatus != PackageStatus.OUT_FOR_DELIVERY) {
            throw new IllegalStateException("Must move to OUT_FOR_DELIVERY");
        }

        if (currentStatus == PackageStatus.OUT_FOR_DELIVERY &&
                !(newStatus == PackageStatus.DELIVERED || newStatus == PackageStatus.FAILED)) {
            throw new IllegalStateException("Must DELIVER or FAIL");
        }


        //Update Package Status
        pkg.setPackageStatus(newStatus);

        //Update Assignment Status + Timm
        if (newStatus == PackageStatus.PICKED) {
            assignment.setPickedAt(now);
            assignment.setDeliveryAssignmentStatus(DeliveryAssignmentStatus.IN_PROGRESS);
        }

        if (newStatus == PackageStatus.DELIVERED) {
            assignment.setDeliveredAt(now);
            assignment.setDeliveryAssignmentStatus(DeliveryAssignmentStatus.DELIVERED);
        }

        if (newStatus == PackageStatus.FAILED) {
            assignment.setDeliveryAssignmentStatus(DeliveryAssignmentStatus.FAILED);
        }

        // 7. Save
        packageRepository.save(pkg);
        deliveryAssignmentRepository.save(assignment);

        // 8. Create Tracking Entry
        PackageTracking tracking = PackageTracking.builder()
                .pkg(pkg)
                .packageStatus(newStatus)
                .timeStamp(now)
                .location(pkg.getOrder().getLocation())
                .build();

        packageTrackingRepository.save(tracking);

        //Update Order status based on packages
        Order order = pkg.getOrder();

        List<Package> allPackages = order.getPackages();

        boolean allDelivered = allPackages.stream().allMatch(p -> p.getPackageStatus() == PackageStatus.DELIVERED);

        boolean anyInTransit = allPackages.stream().anyMatch(p ->
                p.getPackageStatus() == PackageStatus.PICKED ||
                p.getPackageStatus() == PackageStatus.CREATED ||
                p.getPackageStatus() == PackageStatus.OUT_FOR_DELIVERY);

        boolean anyFailed = allPackages.stream().anyMatch(p -> p.getPackageStatus() == PackageStatus.FAILED);

        if (allDelivered){
            order.setOrderStatus(OrderStatus.DELIVERED);
        }else if(anyFailed){
            order.setOrderStatus(OrderStatus.CANCELED);
        } else if (anyInTransit) {
            order.setOrderStatus(OrderStatus.SHIPPED);
        }else {
            order.setOrderStatus(OrderStatus.CONFIRMED);
        }

        orderRepository.save(order);

        // 9. Response
        return UpdateDeliveryStatusResponseDTO.builder()
                .packageId(pkg.getPackageId())
                .status(newStatus.name())
                .updatedAt(now)
                .build();
    }
}
