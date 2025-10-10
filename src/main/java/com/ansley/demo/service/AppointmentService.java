package com.ansley.demo.service;

import com.ansley.demo.model.Appointment;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class AppointmentService {
    private static final String COLLECTION = "appointments";

    private Firestore getDb() {
        return FirestoreClient.getFirestore();
    }

    public List<Appointment> getAllAppointments() {
        try {
            ApiFuture<QuerySnapshot> future = getDb().collection(COLLECTION).get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            List<Appointment> list = new ArrayList<>();
            for (QueryDocumentSnapshot doc : docs) {
                list.add(doc.toObject(Appointment.class));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching appointments: " + e.getMessage());
        }
    }

    public Appointment bookAppointment(Appointment appointment) {
        try {
            if (appointment.getStatus() == null) appointment.setStatus("pending");
            getDb().collection(COLLECTION).add(appointment).get();
            return appointment;
        } catch (Exception e) {
            throw new RuntimeException("Error booking appointment: " + e.getMessage());
        }
    }

    public void verifyAppointment(Long appointmentId) {
        try {
            ApiFuture<QuerySnapshot> future = getDb().collection(COLLECTION)
                    .whereEqualTo("id", appointmentId).get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            if (docs.isEmpty()) throw new RuntimeException("Appointment not found");
            docs.get(0).getReference().update(Map.of("verified", true, "status", "confirmed"));
        } catch (Exception e) {
            throw new RuntimeException("Error verifying appointment: " + e.getMessage());
        }
    }
}
