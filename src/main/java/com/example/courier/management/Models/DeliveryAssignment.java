package com.example.courier.management.Models;

import com.example.courier.management.Models.Type.DeliveryAssignmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "delivery_assignment")
public class DeliveryAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assigned_id")
    private Integer assignedId;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "picked_at")
    private LocalDateTime pickedAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "delivery_assignment_status")
    @Enumerated(EnumType.STRING)
    private DeliveryAssignmentStatus deliveryAssignmentStatus;

    @ManyToOne()
    @JoinColumn(name = "agent_id", referencedColumnName = "user_id")
    private User agent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", referencedColumnName = "package_id", nullable = false)
    private Package pkg;

}
