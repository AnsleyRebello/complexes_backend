package com.ansley.demo.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    // Stub: just print to console
    public void sendVerificationEmail(Long appointmentId, Long userId) {
        System.out.println("Sending verification email for appointment " + appointmentId + " to user " + userId);
    }
}
