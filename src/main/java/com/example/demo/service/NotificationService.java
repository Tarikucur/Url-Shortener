package com.example.demo.service;

import com.example.demo.enums.AccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public NotificationService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendWelcomeNotification(String identifier, AccountType accountType) {
        String topic = "userTopic";
        String msg = (accountType == AccountType.EMAIL)
                ? "Welcome! You've registered with your email: " + identifier
                : "Welcome! You've registered with your phone number: " + identifier;
        kafkaTemplate.send(topic, msg);
    }
}
