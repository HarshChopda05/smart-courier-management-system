package com.example.courier.management.Services.Implementations;

import com.example.courier.management.Exceptions.ResourceNotFoundException;
import com.example.courier.management.Models.*;
import com.example.courier.management.Models.Package;
import com.example.courier.management.Models.Type.DeliveryAssignmentStatus;
import com.example.courier.management.Models.Type.PackageStatus;
import com.example.courier.management.Models.Type.RoleType;
import com.example.courier.management.PayLoads.*;
import com.example.courier.management.Repositories.*;
import com.example.courier.management.Services.DeliveryAssignmentService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryAssignmentImpl implements DeliveryAssignmentService {

    private final DeliveryAssignmentRepository deliveryAssignmentRepository;
    private final PackageRepository packageRepository;
    private final UserRepository userRepository;
    private final PackageTrackingRepository packageTrackingRepository;
    private final AsyncAssignmentServiceImpl asyncAssignmentService;



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
       // List<DeliveryAssignment> newAssignments = new ArrayList<>();
        List<CompletableFuture<DeliveryAssignment>> futures = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

       // List<Integer> assignedIds = deliveryAssignmentRepository.findAssignedPackageIds(requestDTO.getPackageIds());

        for (Package pkg : packages) {

            if (pkg.getPackageStatus() == PackageStatus.DELIVERED) {
                throw new IllegalStateException("Package already delivered: " + pkg.getPackageId());
            }

            //Check existing assignments
            List<DeliveryAssignment> existingAssignments = deliveryAssignmentRepository.findByPkg(pkg);

            boolean hasActiveAssignment = existingAssignments.stream().anyMatch(a ->
                    a.getDeliveryAssignmentStatus() == DeliveryAssignmentStatus.ASSIGNED ||
                            a.getDeliveryAssignmentStatus() == DeliveryAssignmentStatus.IN_PROGRESS
            );

            if (hasActiveAssignment) {
                throw new IllegalStateException("Package already actively assigned: " + pkg.getPackageId());
            }

            // If FAILED → allow reassignment
            boolean wasFailed = existingAssignments.stream().anyMatch(a ->
                    a.getDeliveryAssignmentStatus() == DeliveryAssignmentStatus.FAILED
            );

            if (wasFailed) {
                // Reset package status
                pkg.setPackageStatus(PackageStatus.CREATED);
            }

            //async CALL
            futures.add(asyncAssignmentService.assignAsync(pkg, agent));

            // Add tracking entry
            PackageTracking tracking = PackageTracking.builder()
                    .pkg(pkg)
                    .packageStatus(PackageStatus.CREATED)
                    .timeStamp(now)
                    .location(pkg.getOrder().getLocation())
                    .build();

            trackingList.add(tracking);

        }

        // wait all
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // collect results
        assignmentList = futures.stream()
                .map(CompletableFuture::join)
                .toList();

        //batch wise save
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

    @Override
    public PageResponseDTO<PackageResponseDTO> getAllPackages(int page, int size, String sortBy, String sortDir) {

        if (page < 0) throw new IllegalArgumentException("Page cannot be negative");
        if (size <= 0) throw new IllegalArgumentException("Size must be greater than 0");

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Package> packagePage = packageRepository.findAll(pageable);

        List<PackageResponseDTO> packageDTOs = packagePage.getContent()
                .stream().map(packages -> PackageResponseDTO.builder()
                        .packageId(packages.getPackageId())
                        .packageName(packages.getPackageName())
                        .weight(packages.getWeight())
                        .packageStatus(packages.getPackageStatus().name()).build())
                .toList();

        return PageResponseDTO.<PackageResponseDTO>builder()
                .content(packageDTOs)
                .page(packagePage.getSize())
                .size(packagePage.getSize())
                .totalElements(packagePage.getTotalElements())
                .totalPages(packagePage.getTotalPages())
                .last(packagePage.isLast())
                .build();
    }

    @Override
    public PageResponseDTO<DeliveryAssignmentResponseDTO> getAllAssignments(int page, int size, String sortBy, String sortDir) {

        if (page < 0) throw new IllegalArgumentException("Page cannot be negative");
        if (size <= 0) throw new IllegalArgumentException("Size must be greater than 0");

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<DeliveryAssignment> assignmentPage = deliveryAssignmentRepository.findAll(pageable);


        List<DeliveryAssignmentResponseDTO> assignmentDTOs = assignmentPage.getContent()
                .stream().map(assignment -> DeliveryAssignmentResponseDTO.builder()
                        .assignmentId(assignment.getAssignedId())
                        .packageId(assignment.getPkg().getPackageId())
                        .agentId(assignment.getAgent().getUserId())
                        .status(assignment.getDeliveryAssignmentStatus().name())
                        .assignedAt(assignment.getAssignedAt()).build())
                .toList();

        return PageResponseDTO.<DeliveryAssignmentResponseDTO>builder()
                .content(assignmentDTOs)
                .page(assignmentPage.getNumber())
                .size(assignmentPage.getSize())
                .totalElements(assignmentPage.getTotalElements())
                .totalPages(assignmentPage.getTotalPages())
                .last(assignmentPage.isLast())
                .build();

    }

    @Override
    public PageResponseDTO<AgentResponseDTO> getAllAgents(int page, int size, String sortBy, String sortDir) {

        if (page < 0) throw new IllegalArgumentException("Page cannot be negative");
        if (size <= 0) throw new IllegalArgumentException("Size must be greater than 0");

        //Validation
        List<String> allowedSortFields = List.of("userId", "email");
        if (!allowedSortFields.contains(sortBy)) {
            sortBy = "userId";
        }

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);


        Page<User> agentPage = userRepository.findAllAgents(pageable);

        List<AgentResponseDTO> agentDTOs = agentPage.getContent()
                .stream().map(agent -> AgentResponseDTO.builder()
                        .agentId(agent.getUserId())
                        .email(agent.getEmail()).build())
                .toList();

        return PageResponseDTO.<AgentResponseDTO>builder()
                .content(agentDTOs)
                .page(agentPage.getNumber())
                .size(agentPage.getSize())
                .totalElements(agentPage.getTotalElements())
                .totalPages(agentPage.getTotalPages())
                .last(agentPage.isLast())
                .build();
    }

    @Override
    public DashboardResponseDTO getDashboard() {

        Long total = packageRepository.countTotalPackages();
        Long delivered = packageRepository.countDelivered();
        Long failed = packageRepository.countFailed();
        Long inTransit = packageRepository.countInTransit();

        return DashboardResponseDTO.builder()
                .totalPackages(total)
                .delivered(delivered)
                .failed(failed)
                .inTransit(inTransit)
                .build();
    }
}
