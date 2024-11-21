package com.epam.recipe.platform.service;

public interface NotificationService {
    void sendNotification(String to, String subject, String text);
}
