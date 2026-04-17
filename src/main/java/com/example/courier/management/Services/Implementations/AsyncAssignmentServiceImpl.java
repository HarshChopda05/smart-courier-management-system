package com.example.courier.management.Services.Implementations;

import com.example.courier.management.Models.DeliveryAssignment;
import com.example.courier.management.Models.Package;
import com.example.courier.management.Models.Type.DeliveryAssignmentStatus;
import com.example.courier.management.Models.User;
import com.example.courier.management.Repositories.DeliveryAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AsyncAssignmentServiceImpl {

    private final DeliveryAssignmentRepository deliveryAssignmentRepository;

    @Async
    public CompletableFuture<DeliveryAssignment> assignAsync(Package pkg, User agent){
        DeliveryAssignment assignment = DeliveryAssignment.builder()
                .pkg(pkg)
                .agent(agent)
                .assignedAt(LocalDateTime.now())
                .deliveryAssignmentStatus(DeliveryAssignmentStatus.ASSIGNED)
                .build();

        DeliveryAssignment saved = deliveryAssignmentRepository.save(assignment);

        return CompletableFuture.completedFuture(saved);
    }
}
