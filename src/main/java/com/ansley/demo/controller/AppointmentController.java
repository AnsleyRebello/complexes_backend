package com.ansley.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ansley.demo.model.Appointment;
import com.ansley.demo.service.AppointmentService;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*") // Configure properly for production
public class AppointmentController {
    @Autowired 
    private AppointmentService appointmentService;
    
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByUserId(userId));
    }

    @GetMapping("/building/{buildingId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByBuilding(@PathVariable Long buildingId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByBuildingId(buildingId));
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long appointmentId) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);
            return ResponseEntity.ok(appointment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@RequestBody Appointment appointment) {
        try {
            Appointment bookedAppointment = appointmentService.bookAppointment(appointment);
            return ResponseEntity.ok(bookedAppointment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{appointmentId}")
    public ResponseEntity<?> updateAppointment(@PathVariable Long appointmentId, @RequestBody Appointment appointment) {
        try {
            Appointment updatedAppointment = appointmentService.updateAppointment(appointmentId, appointment);
            return ResponseEntity.ok(updatedAppointment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<?> updateAppointmentStatus(@PathVariable Long appointmentId, @RequestParam String status) {
        try {
            Appointment updatedAppointment = appointmentService.updateAppointmentStatus(appointmentId, status);
            return ResponseEntity.ok(updatedAppointment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{appointmentId}/verify")
    public ResponseEntity<?> verifyAppointment(@PathVariable Long appointmentId) {
        try {
            Appointment verifiedAppointment = appointmentService.verifyAppointment(appointmentId);
            return ResponseEntity.ok(Map.of(
                "message", "Appointment verified successfully",
                "appointment", verifiedAppointment
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long appointmentId) {
        try {
            appointmentService.deleteAppointment(appointmentId);
            return ResponseEntity.ok(Map.of("message", "Appointment deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}