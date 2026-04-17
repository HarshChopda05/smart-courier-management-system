package com.example.courier.management.Repositories;
import com.example.courier.management.Models.Package;
import com.example.courier.management.Models.Type.PackageStatus;
import com.example.courier.management.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<Package, Integer> {

    @Query(value = "SELECT COUNT(*) FROM packages", nativeQuery = true)
    Long countTotalPackages();

    @Query(value = "SELECT COUNT(*) FROM packages WHERE package_status = 'DELIVERED'", nativeQuery = true)
    Long countDelivered();

    @Query(value = "SELECT COUNT(*) FROM packages WHERE package_status = 'FAILED'", nativeQuery = true)
    Long countFailed();

    @Query(value = "SELECT COUNT(*) FROM packages WHERE package_status IN ('IN_TRANSIT', 'OUT_FOR_DELIVERY')", nativeQuery = true)
    Long countInTransit();

    @Query("""
    SELECT p FROM Package p
    JOIN FETCH p.order o
    JOIN FETCH o.location
    WHERE p.packageStatus NOT IN :statuses """)
    List<Package> findActivePackagesWithOrder(@Param("statuses") List<PackageStatus> statuses);



}
