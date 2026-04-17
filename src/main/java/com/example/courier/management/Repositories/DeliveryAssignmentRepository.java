package com.example.courier.management.Repositories;

import com.example.courier.management.Models.DeliveryAssignment;

import com.example.courier.management.Models.Package;
import com.example.courier.management.Models.Type.DeliveryAssignmentStatus;
import com.example.courier.management.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryAssignmentRepository extends JpaRepository<DeliveryAssignment, Integer> {

    @Query(value = "SELECT da.pkg.packageId FROM DeliveryAssignment da WHERE da.pkg.packageId IN :ids")
    List<Integer> findAssignedPackageIds(List<Integer> ids);

    @Query(value = "SELECT da FROM DeliveryAssignment da JOIN FETCH da.pkg p JOIN FETCH p.order o JOIN FETCH o.location WHERE da.agent = :agent")
    List<DeliveryAssignment> findByAgent(User agent);

    @Query(value = " SELECT * FROM delivery_assignment WHERE package_id = :packageId AND agent_id = :agentId", nativeQuery = true)
    Optional<DeliveryAssignment> findAssignmentByPackageAndAgent(@Param("packageId") Integer packageId,
                                                                 @Param("agentId") Integer agentId);

    @Query(value = """
    SELECT da FROM DeliveryAssignment da
    WHERE da.pkg = :pkg
    AND da.agent = :agent
    AND da.deliveryAssignmentStatus IN :statuses
""")
    Optional<DeliveryAssignment> findActiveAssignment(
            @Param("pkg") Package pkg,
            @Param("agent") User agent,
            @Param("statuses") List<DeliveryAssignmentStatus> statuses
    );


    //for Package reAssignment
    @Query(value = """
        SELECT EXISTS (
            SELECT 1 FROM delivery_assignment
            WHERE package_id = :packageId
            AND delivery_assignment_status IN (:statuses)
        )
        """, nativeQuery = true)
    boolean existsByPackageIdAndStatusIn(
            @Param("packageId") Integer packageId,
            @Param("statuses") List<String> statuses
    );

    List<DeliveryAssignment> findByPkg(Package pkg);


}
