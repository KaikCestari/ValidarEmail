package com.validarEmail.service;

import com.validarEmail.entity.AdminUser;
import com.validarEmail.repository.AdminUserRepository;
import com.validarEmail.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AdminUserRepository adminUserRepository;

    public String login(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        AdminUser user = adminUserRepository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return jwtUtil.generateToken(user.getUsername(), user.getRole());
    }
}
