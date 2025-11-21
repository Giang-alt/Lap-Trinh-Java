package com.evmarketplace.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.evmarketplace.dto.LoginRequest;
import com.evmarketplace.dto.LoginResponse;
import com.evmarketplace.dto.RegisterRequest;
import com.evmarketplace.entity.DataConsumer;
import com.evmarketplace.entity.DataProvider;
import com.evmarketplace.entity.User;
import com.evmarketplace.service.UserService;
import com.evmarketplace.util.JwtUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }
        
        String username = userDetails.getUsername();
        User user = userService.findByUsername(username)
            .orElseThrow(() -> new IllegalStateException("User not found"));
        
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("email", user.getEmail());
        userInfo.put("role", user.getRole().toString());
        
        return ResponseEntity.ok(userInfo);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );
            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            // Find the user from your service to get the full entity
            User user = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found after successful authentication"));

            String token = jwtUtil.generateToken(user.getUsername());
            
            LoginResponse response = new LoginResponse();
            response.setToken(token);
            response.setUserType(user.getRole().toString());
            response.setUserId(user.getId());
            response.setUsername(user.getUsername());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid username or password");
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/register/consumer")
    public ResponseEntity<?> registerConsumer(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            if (userService.existsByUsername(registerRequest.getUsername())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Username already exists");
                return ResponseEntity.badRequest().body(error);
            }
            
            if (userService.existsByEmail(registerRequest.getEmail())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Email already exists");
                return ResponseEntity.badRequest().body(error);
            }
            
            DataConsumer consumer = new DataConsumer(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                registerRequest.getCompanyName(),
                registerRequest.getIndustry()
            );
            
            userService.createUser(consumer);
            
            Map<String, String> success = new HashMap<>();
            success.put("message", "Registration successful");
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/register/provider")
    public ResponseEntity<?> registerProvider(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            if (userService.existsByUsername(registerRequest.getUsername())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Username already exists");
                return ResponseEntity.badRequest().body(error);
            }
            
            if (userService.existsByEmail(registerRequest.getEmail())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Email already exists");
                return ResponseEntity.badRequest().body(error);
            }
            
            DataProvider provider = new DataProvider(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                registerRequest.getCompanyName(),
                DataProvider.DataSourceType.OTHER
            );
            
            userService.createUser(provider);
            
            Map<String, String> success = new HashMap<>();
            success.put("message", "Registration successful");
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
