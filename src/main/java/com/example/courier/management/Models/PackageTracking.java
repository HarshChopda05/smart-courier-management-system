package com.example.courier.management.Models;

import com.example.courier.management.Models.Type.PackageStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "package_tracking")
public class PackageTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tracking_id")
    private Integer trackingId;

    @Column(name = "package_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PackageStatus packageStatus;

    @Column(name = "timeStamp")
    private LocalDateTime timeStamp;

    @ManyToOne()
    @JoinColumn(name = "location_id", referencedColumnName = "location_id")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", referencedColumnName = "package_id")
    private Package pkg;



}
