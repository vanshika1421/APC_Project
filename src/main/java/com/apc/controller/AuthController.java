package com.apc.controller;

import com.apc.auth.AuthService;
import com.apc.auth.UserService;
import com.apc.dto.JwtResponse;
import com.apc.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            boolean authenticated = authService.authenticate(request.getUsername(), request.getPassword());
            if (authenticated) {
                String accessToken = jwtUtil.generateToken(request.getUsername());
                String refreshToken = jwtUtil.generateRefreshToken(request.getUsername());
                return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken, request.getUsername(), "Login successful", true));
            } else {
                return ResponseEntity.badRequest().body(new AuthResponse("Invalid credentials", false));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new AuthResponse("Login error: " + e.getMessage(), false));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody LoginRequest request) {
        try {
            // Check if user already exists
            if (userService.userExists(request.getUsername())) {
                return ResponseEntity.badRequest().body(new AuthResponse("User already exists", false));
            }

            authService.register(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(new AuthResponse("Registration successful", true));
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getMessage().contains("Duplicate entry")) {
                return ResponseEntity.badRequest().body(new AuthResponse("Username already exists", false));
            }
            return ResponseEntity.internalServerError().body(new AuthResponse("Registration error: " + e.getMessage(), false));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody com.apc.dto.RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();
            if (jwtUtil.validateToken(refreshToken, jwtUtil.extractUsername(refreshToken)) && jwtUtil.isRefreshToken(refreshToken)) {
                String username = jwtUtil.extractUsername(refreshToken);
                String newAccessToken = jwtUtil.generateToken(username);
                String newRefreshToken = jwtUtil.generateRefreshToken(username);
                return ResponseEntity.ok(new JwtResponse(newAccessToken, newRefreshToken, username, "Token refreshed", true));
            } else {
                return ResponseEntity.badRequest().body(new AuthResponse("Invalid refresh token", false));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new AuthResponse("Refresh error: " + e.getMessage(), false));
        }
    }

    // DTOs
    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class AuthResponse {
        private String message;
        private boolean success;

        public AuthResponse(String message, boolean success) {
            this.message = message;
            this.success = success;
        }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
    }
}