package com.example.courier.management.Services;

import com.example.courier.management.PayLoads.PageResponseDTO;
import com.example.courier.management.PayLoads.UpdateUserRoleRequestDTO;
import com.example.courier.management.PayLoads.UserResponseDTO;

public interface AdminService {
    
    PageResponseDTO<UserResponseDTO> getAllUsers(int page, int size, String sortBy, String sortDir);

    UserResponseDTO getUserById(Integer id);

    String deleteUser(Integer id);

    UserResponseDTO updateUserRole(Integer id, UpdateUserRoleRequestDTO requestDTO);
}
