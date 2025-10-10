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

    public List<Building> getAllBuildings() {
        Firestore db = FirestoreClient.getFirestore();
        try {
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION).get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            List<Building> list = new ArrayList<>();
            for (QueryDocumentSnapshot doc : docs) {
                list.add(doc.toObject(Building.class));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Error getting buildings: " + e.getMessage());
        }
    }

    public List<Building> filterBuildings(double minCost, double maxCost, String type) {
        List<Building> result = new ArrayList<>();
        for (Building b : getAllBuildings()) {
            if (b.getCost() >= minCost && b.getCost() <= maxCost &&
                (type == null || b.getType().equalsIgnoreCase(type))) {
                result.add(b);
            }
        }
        return result;
    }

    public Building save(Building building) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            db.collection(COLLECTION).add(building).get();
            return building;
        } catch (Exception e) {
            throw new RuntimeException("Error saving building: " + e.getMessage());
        }
    }

    public void delete(Long id) {
        Firestore db = FirestoreClient.getFirestore();
        try {
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION).get();
            for (QueryDocumentSnapshot doc : future.get().getDocuments()) {
                Building b = doc.toObject(Building.class);
                if (b.getId() != null && b.getId().equals(id)) {
                    doc.getReference().delete();
                    return;
                }
            }
            throw new RuntimeException("Building not found");
        } catch (Exception e) {
            throw new RuntimeException("Error deleting building: " + e.getMessage());
        }
    }
}
