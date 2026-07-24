package com.example.courier.management.Services;

import com.example.courier.management.PayLoads.ResponseDTO.PageResponseDTO;
import com.example.courier.management.PayLoads.RequestDTO.UpdateUserRoleRequestDTO;
import com.example.courier.management.PayLoads.ResponseDTO.UserResponseDTO;

public interface AdminService {
    
    PageResponseDTO<UserResponseDTO> getAllUsers(int page, int size, String sortBy, String sortDir);
    UserResponseDTO getUserById(Integer id);
    String deleteUser(Integer id);
    UserResponseDTO updateUserRole(Integer id, UpdateUserRoleRequestDTO requestDTO);
}
