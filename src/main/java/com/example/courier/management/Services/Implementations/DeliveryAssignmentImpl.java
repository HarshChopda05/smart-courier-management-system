package com.example.courier.management.Services.Implementations;

import com.example.courier.management.Exceptions.ResourceNotFoundException;
import com.example.courier.management.Models.*;
import com.example.courier.management.Models.Package;
import com.example.courier.management.Models.Type.DeliveryAssignmentStatus;
import com.example.courier.management.Models.Type.OrderStatus;
import com.example.courier.management.Models.Type.PackageStatus;
import com.example.courier.management.Models.Type.RoleType;
import com.example.courier.management.PayLoads.DeliveryAssignmentRequestDTO;
import com.example.courier.management.PayLoads.DeliveryAssignmentResponseDTO;
import com.example.courier.management.Repositories.*;
import com.example.courier.management.Services.DeliveryAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryAssignmentImpl implements DeliveryAssignmentService {

    private final DeliveryAssignmentRepository deliveryAssignmentRepository;
    private final PackageRepository packageRepository;
    private final UserRepository userRepository;
    private final PackageTrackingRepository packageTrackingRepository;

    @Transactional
    @Override
    public List<DeliveryAssignmentResponseDTO> assignPackage(DeliveryAssignmentRequestDTO requestDTO) {

        User agent = userRepository.findById(requestDTO.getAgentId()).orElseThrow(() ->
                new UsernameNotFoundException("Agent Not Found!"));

        //validate agentRole
        if (!agent.getRoles().contains(RoleType.AGENT)) {
            throw new IllegalArgumentException("User is not an agent");
        }

        // Fetch all packages in one query
        List<Package> packages = packageRepository.findAllById(requestDTO.getPackageIds());

        if (packages.size() != requestDTO.getPackageIds().size()) {
            throw new ResourceNotFoundException("Some packages not found");
        }

        List<DeliveryAssignment> assignmentList = new ArrayList<>();
        List<PackageTracking> trackingList = new ArrayList<>();


        List<Integer> assignedIds = deliveryAssignmentRepository.findAssignedPackageIds(requestDTO.getPackageIds());
        if (!assignedIds.isEmpty()) {
            throw new IllegalStateException("Packages already assigned: " + assignedIds);
        }

        //Current DateTime
        LocalDateTime now = LocalDateTime.now();
        for (Package pkg : packages) {

            if (pkg.getPackageStatus() == PackageStatus.DELIVERED) {
                throw new IllegalStateException("Package already delivered: " + pkg.getPackageId());
            }

            //create Assignment
            DeliveryAssignment assignment = DeliveryAssignment.builder()
                    .pkg(pkg)
                    .agent(agent)
                    .assignedAt(now)
                    .deliveryAssignmentStatus(DeliveryAssignmentStatus.ASSIGNED)
                    .build();

            assignmentList.add(assignment);

            //update package status
/*            pkg.setPackageStatus(PackageStatus.IN_TRANSIT);

            //Create tracking Entry
            PackageTracking tracking = PackageTracking.builder()
                    .pkg(pkg)
                    .packageStatus(PackageStatus.IN_TRANSIT)
                    .timeStamp(now)
                    .location(pkg.getOrder().getLocation()) //delivery location
                    .build();

            trackingList.add(tracking);

 */
        }

        //batch wise save
        deliveryAssignmentRepository.saveAll(assignmentList);
        packageRepository.saveAll(packages);
        packageTrackingRepository.saveAll(trackingList);

        // response
        return assignmentList.stream()
                .map(assignment -> DeliveryAssignmentResponseDTO.builder()
                        .assignmentId(assignment.getAssignedId())
                        .packageId(assignment.getPkg().getPackageId())
                        .agentId(agent.getUserId())
                        .status(assignment.getDeliveryAssignmentStatus().name())
                        .assignedAt(assignment.getAssignedAt())
                        .build()
                ).toList();
    }
}
