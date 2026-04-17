package com.example.courier.management.Models;

import com.example.courier.management.Models.Type.RoleType;
import com.example.courier.management.Security.RolePermissionMapping;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Size(min = 3, max = 100, message = "Please enter valid User Name")
    @NotBlank(message = "User Name must required!")
    @Column(name = "user_name", nullable = false)
    private String userName;

    @NotBlank(message = "Email is required!")
    @Email(message = "Please enter a valid email address")
    @Column(name = "email", unique = true)
    private String email;

    @NotBlank(message = "Password is required!")
    @Size(min = 6, message = "Password must be include at least 6 digits and characters")
    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    Set<RoleType> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToMany(mappedBy = "agent")
    private List<DeliveryAssignment> deliveryAssignments;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        roles.forEach(
                role -> {
                    Set<SimpleGrantedAuthority> permissions = RolePermissionMapping.getAuthoritiesForRole(role);
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name())); // ROLE_CUSTOMER
                    authorities.add(new SimpleGrantedAuthority(role.name()));   // CUSTOMER
                    authorities.addAll(permissions);

                }
        );
        return authorities;

    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override public boolean isAccountNonExpired() { return true; }   //Ex: Subscription expired
    @Override public boolean isAccountNonLocked() { return true; }    //Ex: 5 wrong password attempts → lock account
    @Override public boolean isCredentialsNonExpired() { return true; } //Ex: “Change password every 90 days”
    @Override public boolean isEnabled() { return true; }

    //Ex: Email not verified


}
