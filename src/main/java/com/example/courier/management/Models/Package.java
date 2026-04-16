package com.example.courier.management.Models;

import com.example.courier.management.Models.Type.PackageStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "packages")
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id")
    private Integer packageId;

    @NotBlank(message = "Package name is required!")
    @Column(name = "package_name")
    private String packageName;

    @NotNull
    @Positive(message = "Weight must be greater than 0!")
    @Column(name = "weight")
    private Double weight;

    @Column(name = "package_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PackageStatus packageStatus = PackageStatus.CREATED;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false)
    private Order order;

 /*   orphanRemoval: of parent-> child having a connected date, and we delete a data from parent table-
      then associate with data automatically delete from child table.
  */

    @OneToMany(mappedBy = "pkg", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PackageTracking> packageTrackings;

    @OneToMany(mappedBy = "pkg", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryAssignment> deliveryAssignments;

}
