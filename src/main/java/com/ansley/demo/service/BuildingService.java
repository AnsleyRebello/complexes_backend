package com.ansley.demo.service;

import com.ansley.demo.model.Building;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class BuildingService {
    private static final String COLLECTION = "buildings";

    private Firestore getDb() {
        return FirestoreClient.getFirestore();
    }

    // ✅ getAllBuildings
    public List<Building> getAllBuildings() {
        try {
            ApiFuture<QuerySnapshot> future = getDb().collection(COLLECTION).get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            List<Building> list = new ArrayList<>();
            for (QueryDocumentSnapshot doc : docs) {
                Building b = doc.toObject(Building.class);
                list.add(b);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Error getting buildings: " + e.getMessage());
        }
    }

    // ✅ getBuildingById
    public Optional<Building> getBuildingById(Long id) {
        try {
            ApiFuture<QuerySnapshot> future = getDb().collection(COLLECTION)
                    .whereEqualTo("id", id)
                    .get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            if (docs.isEmpty()) return Optional.empty();
            return Optional.of(docs.get(0).toObject(Building.class));
        } catch (Exception e) {
            throw new RuntimeException("Error getting building by ID: " + e.getMessage());
        }
    }

    // ✅ save
    public Building save(Building building) {
        try {
            getDb().collection(COLLECTION).add(building).get();
            return building;
        } catch (Exception e) {
            throw new RuntimeException("Error saving building: " + e.getMessage());
        }
    }

    // ✅ delete
    public void delete(Long id) {
        try {
            ApiFuture<QuerySnapshot> future = getDb().collection(COLLECTION)
                    .whereEqualTo("id", id)
                    .get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            if (docs.isEmpty()) throw new RuntimeException("Building not found");
            docs.get(0).getReference().delete();
        } catch (Exception e) {
            throw new RuntimeException("Error deleting building: " + e.getMessage());
        }
    }

    // ✅ filterBuildings
    public List<Building> filterBuildings(double minCost, double maxCost, String type) {
        List<Building> filtered = new ArrayList<>();
        for (Building b : getAllBuildings()) {
            if (b.getCost() >= minCost && b.getCost() <= maxCost &&
                (type == null || b.getType().equalsIgnoreCase(type))) {
                filtered.add(b);
            }
        }
        return filtered;
    }
}
