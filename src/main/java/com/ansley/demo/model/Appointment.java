package com.ansley.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

import java.time.LocalDateTime;

@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private Long buildingId;
    
    @Column(nullable = false)
    private String appointmentTime;
    
    @Column(nullable = false)
    private boolean verified = false;
    
    @Column(nullable = false)
    private String mode = "physical"; // online or physical
    
    // FIXED: Make nullable true so existing rows won't break
    @Column(nullable = true)
    private String status = "pending"; // pending, confirmed, cancelled, completed
    
    private String notes; // Additional notes for the appointment
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // Constructors
    public Appointment() { super(); }
    
    public Appointment(Long userId, Long buildingId, String appointmentTime, String mode) {
        this.userId = userId;
        this.buildingId = buildingId;
        this.appointmentTime = appointmentTime;
        this.mode = mode;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getBuildingId() { return buildingId; }
    public void setBuildingId(Long buildingId) { this.buildingId = buildingId; }
    public String getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }
    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { 
        this.verified = verified; 
        if (verified) this.updatedAt = LocalDateTime.now();
    }
    public String getMode() { return mode; }
    public void setMode(String mode) { 
        this.mode = mode; 
        this.updatedAt = LocalDateTime.now();
    }
    public String getStatus() { return status; }
    public void setStatus(String status) { 
        this.status = status; 
        this.updatedAt = LocalDateTime.now();
    }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { 
        this.notes = notes; 
        this.updatedAt = LocalDateTime.now();
    }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Appointment [id=" + id + ", userId=" + userId + ", buildingId=" + buildingId 
            + ", appointmentTime=" + appointmentTime + ", verified=" + verified 
            + ", mode=" + mode + ", status=" + status + ", notes=" + notes 
            + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }
}
