package com.ansley.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ansley.demo.model.User;
import com.ansley.demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    public User register(User user) {
        // Check if user already exists
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
    
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User addFavorite(Long userId, Long buildingId) {
        User user = userRepo.findById(userId).orElseThrow(
            () -> new RuntimeException("User not found")
        );
        
        if (user.getFavoriteBuildingIds() != null) {
            if (!user.getFavoriteBuildingIds().contains(buildingId)) {
                user.getFavoriteBuildingIds().add(buildingId);
            }
        } else {
            user.setFavoriteBuildingIds(List.of(buildingId));
        }
        
        return userRepo.save(user);
    }

    public User removeFavorite(Long userId, Long buildingId) {
        User user = userRepo.findById(userId).orElseThrow(
            () -> new RuntimeException("User not found")
        );
        
        if (user.getFavoriteBuildingIds() != null) {
            user.getFavoriteBuildingIds().remove(buildingId);
        }
        
        return userRepo.save(user);
    }

    public Map<String, Object> login(String email, String password) {
        Optional<User> userOpt = userRepo.findByEmail(email);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("userId", user.getId());
                response.put("name", user.getName());
                response.put("email", user.getEmail());
                response.put("favoriteBuildingIds", user.getFavoriteBuildingIds());
                response.put("token", "jwt-token-" + user.getId()); // Generate proper JWT in production
                return response;
            }
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Invalid credentials");
        return response;
    }

    public Optional<User> getUserById(Long userId) {
        return userRepo.findById(userId);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User updateUser(Long userId, User updatedUser) {
        User user = userRepo.findById(userId).orElseThrow(
            () -> new RuntimeException("User not found")
        );
        
        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }
        if (updatedUser.getPhone() != null) {
            user.setPhone(updatedUser.getPhone());
        }
        // Note: Don't update email or password without proper validation
        
        return userRepo.save(user);
    }

    public void deleteUser(Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        userRepo.deleteById(userId);
    }
}