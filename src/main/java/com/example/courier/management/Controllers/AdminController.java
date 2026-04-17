package com.example.courier.management.Controllers;

import com.example.courier.management.PayLoads.PageResponseDTO;
import com.example.courier.management.PayLoads.UpdateUserRoleRequestDTO;
import com.example.courier.management.PayLoads.UserResponseDTO;
import com.example.courier.management.Services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courier/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<PageResponseDTO<UserResponseDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "userId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return new ResponseEntity<>(adminService.getAllUsers(page, size, sortBy, sortDir),HttpStatus.OK);

    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(adminService.getUserById(id));

    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok("User Deleted Successfully.");
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<UserResponseDTO> updateUserRole(
            @PathVariable Integer id,
            @RequestBody UpdateUserRoleRequestDTO requestDTO) {

        return ResponseEntity.ok(adminService.updateUserRole(id, requestDTO));


    }
}
