package com.ansley.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ansley.demo.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
