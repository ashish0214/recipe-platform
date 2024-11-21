package com.epam.recipe.platform.service;

import com.epam.recipe.platform.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendNotification() {

        notificationService.sendNotification("test@example.com", "Test Subject", "Test Body");
        ArgumentCaptor<SimpleMailMessage> mailMessageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender).send(mailMessageCaptor.capture());
        SimpleMailMessage sentMessage = mailMessageCaptor.getValue();

        assertEquals("test@example.com", Objects.requireNonNull(sentMessage.getTo())[0]);
        assertEquals("Test Subject", sentMessage.getSubject());
        assertEquals("Test Body", sentMessage.getText());
    }

    @Test
    void sendNotification_Exception() {
        doThrow(new RuntimeException("Mail server error")).when(javaMailSender).send(any(SimpleMailMessage.class));
        try {
            notificationService.sendNotification("test@example.com", "Test Subject", "Test Message");
        } catch (Exception e) {
            assertEquals("Mail server error", e.getMessage());
        }

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendNotification_Success() {

        String to = "recipient@example.com";
        String subject = "Test Subject";
        String text = "Test Message";

        notificationService.sendNotification(to, text, subject);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
    @Test
    public void testSendNotification_Failure() {
        // Given
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String text = "Test Message";

        doThrow(new RuntimeException("Simulated email sending failure")).when(javaMailSender).send(any(SimpleMailMessage.class));

        assertThrows(RuntimeException.class, () -> {
            notificationService.sendNotification(to, text, subject);
        });
    }
}

