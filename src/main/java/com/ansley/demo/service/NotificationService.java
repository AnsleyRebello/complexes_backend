package com.ansley.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ansley.demo.model.Notification;
import com.ansley.demo.repository.NotificationRepository;

@Service
public class NotificationService {
    @Autowired private NotificationRepository notificationRepo;

    public void sendNotification(Long userId, String type, String message) {
        Notification n = new Notification();
        n.setUserId(userId);
        n.setType(type);
        n.setMessage(message);
        n.setSent(false);
        notificationRepo.save(n);
        // optional: trigger email/SMS sending here
    }
}
