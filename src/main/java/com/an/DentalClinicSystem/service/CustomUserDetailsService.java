package com.an.DentalClinicSystem.service;

import com.an.DentalClinicSystem.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private AuthService authService;

    CustomUserDetailsService(AuthService authService){
        this.authService = authService;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = authService.findUserByUserName(username);
        return CustomUserDetails
                .builder()
                .user(user)
                .build();
    }
}
