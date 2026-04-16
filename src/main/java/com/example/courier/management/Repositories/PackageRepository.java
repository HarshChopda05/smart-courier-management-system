package com.example.courier.management.Repositories;
import com.example.courier.management.Models.Package;
import com.example.courier.management.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<Package, Integer> {


}
