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

    public List<Appointment> getAllAppointments() {
        Firestore db = FirestoreClient.getFirestore();
        try {
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION).get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            List<Appointment> list = new ArrayList<>();
            for (QueryDocumentSnapshot doc : docs) {
                Appointment a = doc.toObject(Appointment.class);
                a.setId(Long.valueOf(doc.getId().hashCode()));
                list.add(a);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching appointments: " + e.getMessage());
        }
    }

    public Appointment bookAppointment(Appointment appointment) {
        try {
            if (appointment.getStatus() == null) appointment.setStatus("pending");
            Firestore db = FirestoreClient.getFirestore();
            db.collection(COLLECTION).add(appointment).get();
            return appointment;
        } catch (Exception e) {
            throw new RuntimeException("Error booking appointment: " + e.getMessage());
        }
    }

    public void verifyAppointment(Long appointmentId) {
        Firestore db = FirestoreClient.getFirestore();
        try {
            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION).get();
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : docs) {
                Appointment a = doc.toObject(Appointment.class);
                if (a.getId() != null && a.getId().equals(appointmentId)) {
                    doc.getReference().update(Map.of("verified", true, "status", "confirmed"));
                    return;
                }
            }
            throw new RuntimeException("Appointment not found");
        } catch (Exception e) {
            throw new RuntimeException("Error verifying appointment: " + e.getMessage());
        }
    }
}
