package com.meichel.alert_service.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.meichel.alert_service.dto.DeviceEnergyUsage;
import com.meichel.alert_service.entity.EmailNotification;
import com.meichel.alert_service.repository.EmailNotificationRepo;
import com.meichel.kafka_event.UsageAlertEvent;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlertService {

    private final EmailNotificationRepo emailNotificationRepo;
    private final EmailService emailService;

    private int sendAlertHour=12;

    public AlertService(EmailNotificationRepo emailNotificationRepo, EmailService emailService) {
        this.emailNotificationRepo = emailNotificationRepo;
        this.emailService = emailService;
    }

    @KafkaListener(topics = "usage-alerts", groupId = "alert-service-group")
    public void consumeUsageAlerts(UsageAlertEvent event) {
        log.info("Consumed event from Kafka: {}", event);

        boolean recentlyNotified = emailNotificationRepo.existsByUserIdAndSentTrueAndTimestampAfter(
                event.getUserId(), LocalDateTime.now().minusHours(sendAlertHour));

        if (recentlyNotified) {
            log.info("Skipping alert for user {} - already notified within the last {} hours",
                    event.getUserId(), sendAlertHour);
            return;
        }

        String htmlMessage = buildHtmlMessage(event);
        boolean sendSucceeded;

        try {
            emailService.sendHtml(event.getEmail(), "Energy Usage Alert", htmlMessage);
            log.info("Email sent to {} successfully", event.getEmail());
            sendSucceeded = true;
        } catch (MessagingException e) {
            log.error("Error sending email to {} -> {}", event.getEmail(), e.getMessage());
            sendSucceeded = false;
        } catch (Exception e) {
            log.error("Unexpected error sending email to {} -> {}", event.getEmail(), e.getMessage());
            sendSucceeded = false;
        }

        EmailNotification notification = EmailNotification.builder()
                .userId(event.getUserId())
                .message(htmlMessage)
                .sent(sendSucceeded)
                .timestamp(LocalDateTime.now())
                .build();

        emailNotificationRepo.save(notification);
    }

    private String buildHtmlMessage(UsageAlertEvent event) {
        StringBuilder rows = new StringBuilder();
    
        if (event.getDevices() != null) {
            for (DeviceEnergyUsage device : event.getDevices()) {
                rows.append(String.format("""
                        <tr>
                          <td>%s</td>
                          <td>%s</td>
                          <td>%.2f kWh</td>
                        </tr>
                        """,
                        device.getName(), device.getType(), device.getEnergyConsumed()));
            }
        }
    
        return String.format("""
                <html>
                  <body>
                    <h2>Energy Usage Alert</h2>
                    <p>
                      Your devices consumed <strong>%.2f kWh</strong> in the last hour,
                      exceeding your threshold of <strong>%d kWh</strong>.
                    </p>
    
                    <h3>Breakdown by device</h3>
                    <table border="1">
                      <thead>
                        <tr>
                          <th>Device</th>
                          <th>Type</th>
                          <th>Consumed</th>
                        </tr>
                      </thead>
                      <tbody>
                        %s
                      </tbody>
                    </table>
    
                    <p>
                      You're receiving this because energy alerting is enabled on your account.
                    </p>
                  </body>
                </html>
                """,
                event.getTotalConsumed(), event.getThreshold(), rows.toString());
    }
}