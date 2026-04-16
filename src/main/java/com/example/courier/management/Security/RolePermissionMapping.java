package com.example.courier.management.Security;

import com.example.courier.management.Models.Type.PermissionType;
import com.example.courier.management.Models.Type.RoleType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.courier.management.Models.Type.PermissionType.*;
import static com.example.courier.management.Models.Type.RoleType.*;

public class RolePermissionMapping {
    private static final Map<RoleType, Set<PermissionType>> rolePermissionMap = Map.of(

            ADMIN, Set.of(
                    MANAGE_USERS, CREATE_USER, VIEW_USERS, UPDATE_USER, DELETE_USER,
                    ASSIGN_DELIVERY, VIEW_ALL_DELIVERIES, MONITOR_DELIVERY_AGENTS,
                    VIEW_ASSIGNED_DELIVERIES, UPDATE_DELIVERY_STATUS,
                    PLACE_ORDER, VIEW_OWN_ORDERS, TRACK_PACKAGE),

            MANAGER, Set.of(ASSIGN_DELIVERY, VIEW_ALL_DELIVERIES, MONITOR_DELIVERY_AGENTS),
            AGENT, Set.of(VIEW_ASSIGNED_DELIVERIES, UPDATE_DELIVERY_STATUS),
            CUSTOMER, Set.of(PLACE_ORDER, VIEW_OWN_ORDERS, TRACK_PACKAGE)
    );

    //Convert permissions → authorities
    public static Set<SimpleGrantedAuthority> getAuthoritiesForRole(RoleType role) {

        return rolePermissionMap.getOrDefault(role, Set.of())
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
    }

}
