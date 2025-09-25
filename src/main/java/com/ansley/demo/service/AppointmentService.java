package com.ansley.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ansley.demo.model.Appointment;
import com.ansley.demo.repository.AppointmentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    @Autowired 
    private AppointmentRepository appointmentRepo;

    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }

    public List<Appointment> getAppointmentsByUserId(Long userId) {
        return appointmentRepo.findByUserId(userId);
    }

    public List<Appointment> getAppointmentsByBuildingId(Long buildingId) {
        return appointmentRepo.findByBuildingId(buildingId);
    }

    public Appointment bookAppointment(Appointment appointment) {
        // Set default status if not provided
        if (appointment.getStatus() == null) {
            appointment.setStatus("pending");
        }
        
        // Validate required fields
        if (appointment.getUserId() == null) {
            throw new RuntimeException("User ID is required");
        }
        if (appointment.getBuildingId() == null) {
            throw new RuntimeException("Building ID is required");
        }
        if (appointment.getAppointmentTime() == null) {
            throw new RuntimeException("Appointment time is required");
        }
        
        return appointmentRepo.save(appointment);
    }

    public Appointment verifyAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepo.findById(appointmentId)
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        appointment.setVerified(true);
        appointment.setStatus("confirmed");
        
        return appointmentRepo.save(appointment);
    }

    public Appointment updateAppointmentStatus(Long appointmentId, String status) {
        Appointment appointment = appointmentRepo.findById(appointmentId)
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        appointment.setStatus(status);
        
        // Auto-verify if confirmed
        if ("confirmed".equals(status)) {
            appointment.setVerified(true);
        }
        
        return appointmentRepo.save(appointment);
    }

    public Appointment getAppointmentById(Long appointmentId) {
        return appointmentRepo.findById(appointmentId)
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    public void deleteAppointment(Long appointmentId) {
        if (!appointmentRepo.existsById(appointmentId)) {
            throw new RuntimeException("Appointment not found");
        }
        appointmentRepo.deleteById(appointmentId);
    }

    public Appointment updateAppointment(Long appointmentId, Appointment updatedAppointment) {
        Appointment appointment = appointmentRepo.findById(appointmentId)
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        if (updatedAppointment.getAppointmentTime() != null) {
            appointment.setAppointmentTime(updatedAppointment.getAppointmentTime());
        }
        if (updatedAppointment.getMode() != null) {
            appointment.setMode(updatedAppointment.getMode());
        }
        if (updatedAppointment.getStatus() != null) {
            appointment.setStatus(updatedAppointment.getStatus());
        }
        
        return appointmentRepo.save(appointment);
    }
}