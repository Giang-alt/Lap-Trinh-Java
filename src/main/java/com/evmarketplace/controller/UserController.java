package com.evmarketplace.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evmarketplace.entity.User;
import com.evmarketplace.service.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateUserStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {
        try {
            String statusValue = payload.get("status");
            if (statusValue == null || statusValue.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Status is required"));
            }

            return userService.findById(id)
                    .map(user -> {
                        try {
                            User.UserStatus newStatus = User.UserStatus.valueOf(statusValue.toUpperCase());
                            user.setStatus(newStatus);
                            userService.updateUser(user);
                            return ResponseEntity.ok(user);
                        } catch (IllegalArgumentException e) {
                            return ResponseEntity.badRequest().body(Map.of("error", "Invalid status value"));
                        }
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of("error", "User not found")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update user status: " + e.getMessage()));
        }
    }
}
