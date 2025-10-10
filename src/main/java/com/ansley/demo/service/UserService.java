package com.ansley.demo.service;

import com.ansley.demo.model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    @Autowired private PasswordEncoder passwordEncoder;
    private static final String COLLECTION = "users";

    private Firestore getDb() {
        return FirestoreClient.getFirestore();
    }

    // ✅ register
    public User register(User user) {
        try {
            if (user.getPassword() != null)
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            getDb().collection(COLLECTION).add(user).get();
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Error registering user: " + e.getMessage());
        }
    }

    // ✅ getAllUsers
    public List<User> getAllUsers() {
        try {
            ApiFuture<QuerySnapshot> future = getDb().collection(COLLECTION).get();
            List<User> users = new ArrayList<>();
            for (QueryDocumentSnapshot doc : future.get().getDocuments()) {
                users.add(doc.toObject(User.class));
            }
            return users;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching users: " + e.getMessage());
        }
    }

    // ✅ getUserById
    public Optional<User> getUserById(Long id) {
        try {
            ApiFuture<QuerySnapshot> future = getDb().collection(COLLECTION)
                    .whereEqualTo("id", id).get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            if (docs.isEmpty()) return Optional.empty();
            return Optional.of(docs.get(0).toObject(User.class));
        } catch (Exception e) {
            throw new RuntimeException("Error fetching user: " + e.getMessage());
        }
    }

    // ✅ login
    public Map<String, Object> login(String email, String password) {
        try {
            ApiFuture<QuerySnapshot> future = getDb().collection(COLLECTION)
                    .whereEqualTo("email", email).get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();

            if (docs.isEmpty()) return Map.of("success", false, "message", "Invalid credentials");

            User user = docs.get(0).toObject(User.class);
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Map.of(
                        "success", true,
                        "name", user.getName(),
                        "email", user.getEmail(),
                        "userId", docs.get(0).getId()
                );
            }
            return Map.of("success", false, "message", "Wrong password");
        } catch (Exception e) {
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }

    // ✅ updateUser
    public User updateUser(Long userId, User updatedUser) {
        try {
            Optional<User> existingOpt = getUserById(userId);
            if (existingOpt.isEmpty()) throw new RuntimeException("User not found");

            User existing = existingOpt.get();
            if (updatedUser.getName() != null) existing.setName(updatedUser.getName());
            if (updatedUser.getPhone() != null) existing.setPhone(updatedUser.getPhone());

            getDb().collection(COLLECTION).add(existing).get();
            return existing;
        } catch (Exception e) {
            throw new RuntimeException("Error updating user: " + e.getMessage());
        }
    }

    // ✅ deleteUser
    public void deleteUser(Long userId) {
        try {
            ApiFuture<QuerySnapshot> future = getDb().collection(COLLECTION)
                    .whereEqualTo("id", userId).get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            if (docs.isEmpty()) throw new RuntimeException("User not found");
            docs.get(0).getReference().delete();
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage());
        }
    }

    // ✅ Favorites support (dummy update)
    public User addFavorite(Long userId, Long buildingId) {
        Optional<User> opt = getUserById(userId);
        if (opt.isEmpty()) throw new RuntimeException("User not found");
        User u = opt.get();
        if (u.getFavoriteBuildingIds() == null) u.setFavoriteBuildingIds(new ArrayList<>());
        if (!u.getFavoriteBuildingIds().contains(buildingId))
            u.getFavoriteBuildingIds().add(buildingId);
        getDb().collection(COLLECTION).add(u);
        return u;
    }

    public User removeFavorite(Long userId, Long buildingId) {
        Optional<User> opt = getUserById(userId);
        if (opt.isEmpty()) throw new RuntimeException("User not found");
        User u = opt.get();
        if (u.getFavoriteBuildingIds() != null)
            u.getFavoriteBuildingIds().remove(buildingId);
        getDb().collection(COLLECTION).add(u);
        return u;
    }
}
