package com.an.DentalClinicSystem.service;

import com.an.DentalClinicSystem.entity.User;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;


@Builder
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = user.getRole().getName().toUpperCase(); // hoặc toLowerCase nếu bạn dùng ROLE_patient
        // Giả sử Role là Enum hoặc String, bạn có thể map sang SimpleGrantedAuthority
        return Collections.singleton(() -> "ROLE_" + roleName);
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getUsername(); // hoặc user.getUsername() tùy vào thiết kế
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // hoặc user.isAccountNonExpired()
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // hoặc user.isAccountNonLocked()
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // hoặc user.isCredentialsNonExpired()
    }

    @Override
    public boolean isEnabled() {
        return true; // hoặc user.isEnabled()
    }

    public User getUser() {
        return user;
    }
}
