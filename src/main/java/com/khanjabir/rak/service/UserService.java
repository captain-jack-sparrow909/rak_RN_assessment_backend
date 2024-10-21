package com.khanjabir.rak.service;

import com.khanjabir.rak.dto.DeleteUserRequestDTO;
import com.khanjabir.rak.dto.PasswordChangeRequestDTO;
import com.khanjabir.rak.entity.User;
import com.khanjabir.rak.exception.InvalidPasswordException;
import com.khanjabir.rak.exception.UserNotFoundException;
import com.khanjabir.rak.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found for id  " + id)));
    }

    public User updateUser(Long id, User user) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " not found.");
        }
        user.setId(id);
        return userRepository.save(user);
    }

    public User changeUserPassword(Long id, PasswordChangeRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found."));

        // Validate the user's email
        if (!user.getEmail().equals(request.getEmail())) {
            throw new InvalidPasswordException("Email does not match for the given user ID.");
        }

        // Validate the current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Current password is incorrect.");
        }

        // Set the new password and save
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return userRepository.save(user);
    }

    public void deleteUser(Long id, DeleteUserRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found."));

        // Validate the user's email
        if (!user.getEmail().equals(request.getEmail())) {
            throw new InvalidPasswordException("Email does not match for the given user ID.");
        }

        // Validate the password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Password is incorrect.");
        }

        // Perform the deletion
        userRepository.deleteById(id);
    }
}
