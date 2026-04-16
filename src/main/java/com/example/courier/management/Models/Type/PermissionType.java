package com.example.courier.management.Models.Type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionType {

    //ADMIN
    MANAGE_USERS("ADMIN:Manage Users"),
    CREATE_USER("ADMIN:Create User"),
    VIEW_USERS("ADMIN:View Users"),
    UPDATE_USER("ADMIN:Update User"),
    DELETE_USER("ADMIN:Delete User"),

    //MANAGER
    ASSIGN_DELIVERY("MANAGER:Assign Delivery"),
    VIEW_ALL_DELIVERIES("MANAGER:View All Deliveries"),
    MONITOR_DELIVERY_AGENTS("MANAGER:Monitor Delivery Agents"),

    //DELIVERY AGENT
    VIEW_ASSIGNED_DELIVERIES("AGENTView Assigned Deliveries"),
    UPDATE_DELIVERY_STATUS("AGENTUpdate Delivery Status"),

    //CUSTOMER
    PLACE_ORDER("CUSTOMER:Place Order"),
    VIEW_OWN_ORDERS("CUSTOMER:View Own Orders"),
    TRACK_PACKAGE("CUSTOMER:Track Package");

    private final String permission;

}
