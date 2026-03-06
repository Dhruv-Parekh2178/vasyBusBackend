package com.app.vasyBus.model;

import com.app.vasyBus.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_users", indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_phone", columnList = "phone"),
        @Index(name = "idx_user_role", columnList = "role")
})
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Booking> bookings;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @PrePersist
    @PreUpdate
    private void validatePhone() {
        if (phone != null && !isValidIndianPhoneNumber(phone)) {
            throw new IllegalArgumentException("Invalid Indian phone number. Must start with 6,7,8,9 and be 10 digits");
        }
    }

    private boolean isValidIndianPhoneNumber(String phone) {
        String phoneRegex = "^[6-9]\\d{9}$";
        return Pattern.matches(phoneRegex, phone);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}