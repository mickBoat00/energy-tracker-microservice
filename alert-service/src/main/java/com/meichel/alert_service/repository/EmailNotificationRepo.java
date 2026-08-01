package com.meichel.alert_service.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meichel.alert_service.entity.EmailNotification;

public interface EmailNotificationRepo extends JpaRepository<EmailNotification, Long> {
    boolean existsByUserIdAndSentTrueAndTimestampAfter(Long userId, LocalDateTime cutoff);
}
