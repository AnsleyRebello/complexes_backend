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

    public User register(User user) {
        Firestore db = FirestoreClient.getFirestore();
        try {
            if (user.getPassword() != null)
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            db.collection(COLLECTION).add(user).get();
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Error registering user: " + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        Firestore db = FirestoreClient.getFirestore();
        try {
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION).get();
            List<User> users = new ArrayList<>();
            for (QueryDocumentSnapshot doc : future.get().getDocuments()) {
                users.add(doc.toObject(User.class));
            }
            return users;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching users: " + e.getMessage());
        }
    }

    public Map<String, Object> login(String email, String password) {
        Firestore db = FirestoreClient.getFirestore();
        try {
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION)
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

    public void deleteUser(Long userId) {
        Firestore db = FirestoreClient.getFirestore();
        try {
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION).get();
            for (QueryDocumentSnapshot doc : future.get().getDocuments()) {
                User u = doc.toObject(User.class);
                if (u.getId() != null && u.getId().equals(userId)) {
                    doc.getReference().delete();
                    return;
                }
            }
            throw new RuntimeException("User not found");
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage());
        }
    }
}
