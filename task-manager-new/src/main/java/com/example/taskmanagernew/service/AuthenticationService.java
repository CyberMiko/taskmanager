package com.example.taskmanagernew.service;

import com.example.taskmanagernew.config.JwtService;
import com.example.taskmanagernew.models.AuthenticationRequest;
import com.example.taskmanagernew.models.AuthenticationResponse;
import com.example.taskmanagernew.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthenticationResponse login(AuthenticationRequest request) {
        final UserDetails userDetails = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        final String jwtToken = jwtService.generateToken(userDetails);
        final String refreshToken = jwtService.generateRefreshToken(userDetails);
        final String username = userDetails.getUsername();
        final String role = userDetails.getAuthorities().toString(); // Предполагается, что роли сохранены в authorities

        return new AuthenticationResponse(jwtToken, refreshToken, username, role);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader("Authorization");
        String refreshToken = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            refreshToken = authHeader.substring(7);
            try {
                username = jwtService.extractUsername(refreshToken);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                String newToken = jwtService.generateToken(userDetails);
                response.setHeader("Authorization", "Bearer " + newToken);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
