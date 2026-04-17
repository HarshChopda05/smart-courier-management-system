package com.example.courier.management.Services.Implementations;

import com.example.courier.management.Models.Type.RoleType;
import com.example.courier.management.Models.User;
import com.example.courier.management.PayLoads.PageResponseDTO;
import com.example.courier.management.PayLoads.UpdateUserRoleRequestDTO;
import com.example.courier.management.PayLoads.UserResponseDTO;
import com.example.courier.management.Repositories.DeliveryAssignmentRepository;
import com.example.courier.management.Repositories.OrderRepository;
import com.example.courier.management.Repositories.UserRepository;
import com.example.courier.management.Services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final DeliveryAssignmentRepository deliveryAssignmentRepository;

    @Override
    public PageResponseDTO<UserResponseDTO> getAllUsers(int page, int size, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponseDTO> users = userPage.getContent()
                .stream()
                .map(user -> UserResponseDTO.builder()
                        .userId(user.getUserId())
                        .userName(user.getUsername())
                        .email(user.getEmail())
                        .roles(user.getRoles()
                                .stream()
                                .map(Enum::name)
                                .collect(Collectors.toSet()))
                        .build())
                .toList();

        return PageResponseDTO.<UserResponseDTO>builder()
                .content(users)
                .page(userPage.getNumber())
                .size(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .last(userPage.isLast())
                .build();

    }

    @Override
    public UserResponseDTO getUserById(Integer id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .userName(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles()
                        .stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet()))
                .build();
    }
    @Override
    @Transactional
    public String deleteUser(Integer id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

//        orderRepository.deleteById(id);
//        deliveryAssignmentRepository.deleteById(id);
        userRepository.delete(user);

        return "User deleted successfully!";
    }


    @Override
    public UserResponseDTO updateUserRole(Integer id, UpdateUserRoleRequestDTO requestDTO) {

        User user =userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if ((requestDTO.getRoles()).isEmpty()) {
            throw new IllegalArgumentException("User must have at least one role");
        }

        Set<RoleType> roles = requestDTO.getRoles()
                .stream()
                .map(role -> {
                    try {
                        return RoleType.valueOf(role.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Invalid role: " + role);
                    }
                })
                .collect(Collectors.toSet());

        user.setRoles(roles);

        User updatedUser = userRepository.save(user);

        return UserResponseDTO.builder()
                .userId(updatedUser.getUserId())
                .userName(updatedUser.getUsername())
                .email(updatedUser.getEmail())
                .roles(updatedUser.getRoles()
                        .stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet()))
                .build();

    }
}


