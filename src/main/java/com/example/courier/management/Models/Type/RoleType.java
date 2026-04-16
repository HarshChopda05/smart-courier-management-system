package com.example.courier.management.Models.Type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.management.relation.Role;

public enum RoleType {
    ADMIN,
    MANAGER,
    AGENT,
    CUSTOMER;
}
