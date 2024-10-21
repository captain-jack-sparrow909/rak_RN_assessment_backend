package com.khanjabir.rak.controller;

import com.khanjabir.rak.dto.DeleteUserRequestDTO;
import com.khanjabir.rak.dto.PasswordChangeRequestDTO;
import com.khanjabir.rak.dto.UserDTO;
import com.khanjabir.rak.entity.User;
import com.khanjabir.rak.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) {
        // Map UserDTO to User entity
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword()); // The password will be hashed in the service

        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        // Map UserDTO to User entity
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword()); // The password will be hashed in the service

        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> changeUserPassword(@PathVariable Long id, @Valid @RequestBody PasswordChangeRequestDTO request) {
        return ResponseEntity.ok(userService.changeUserPassword(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, @Valid @RequestBody DeleteUserRequestDTO request) {
        userService.deleteUser(id, request);
        return ResponseEntity.ok("User has been deleted successfully.");
    }
}
