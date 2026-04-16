package com.example.courier.management.Repositories;

import com.example.courier.management.Models.PackageTracking;
import com.example.courier.management.Models.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageTrackingRepository extends JpaRepository<PackageTracking, Integer> {

    List<PackageTracking> findByPkg(Package pkg);
    List<PackageTracking> findByPkgOrderByTimeStampAsc(Package pkg);
}
