package com.ansley.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ansley.demo.model.Appointment;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    // Find appointments by user ID
    List<Appointment> findByUserId(Long userId);
    
    // Find appointments by building ID
    List<Appointment> findByBuildingId(Long buildingId);
    
    // Find appointments by user ID and status
    List<Appointment> findByUserIdAndStatus(Long userId, String status);
    
    // Find appointments by building ID and status
    List<Appointment> findByBuildingIdAndStatus(Long buildingId, String status);
    
    // Find verified appointments
    List<Appointment> findByVerified(boolean verified);
    
    // Find appointments by user ID and verified status
    List<Appointment> findByUserIdAndVerified(Long userId, boolean verified);
}