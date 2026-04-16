package com.example.courier.management.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Integer locationId;

    @NotBlank(message = "Address is required!")
    private String address;

    @NotBlank(message = "City is required!")
    private String city;

    @NotNull
    private Integer pincode;

    @OneToMany(mappedBy = "location")
    private List<Order> orders;

    @OneToMany(mappedBy = "location")
    private List<PackageTracking> packageTrackings;


}
