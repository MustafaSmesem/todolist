package com.comodo.todolistspring.service;

import com.comodo.todolistspring.document.Notification;
import com.comodo.todolistspring.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationQueueTest {

    @Mock private RabbitTemplate rabbitTemplate;
    @Mock private NotificationRepository notificationRepository;
    private NotificationQueue underTest;

    @BeforeEach
    void setUp() {
        underTest = new NotificationQueue(rabbitTemplate, notificationRepository);
    }

    @Test
    void canSendAddNotification() {
        //given
        var notification = new Notification("todoId", "description", new Date());

        // when
        underTest.addNotification(notification);

        // then
        var notificationArgumentCaptor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository).save(notificationArgumentCaptor.capture());
        var capturedNotification = notificationArgumentCaptor.getValue();

        assertThat(capturedNotification).isEqualTo(notification);
    }

    @Test
    void shouldThrowExceptionIfRabbitMQConnectionRefusedForAddNotification() {
        //given
        var notification = new Notification("todoId", "description", new Date());

        // when
        willThrow(new AmqpException("")).given(rabbitTemplate).convertAndSend("exchange", "route", notification.toString());
        underTest.addNotification(notification);

        // then
        verify(notificationRepository, never()).save(any());

    }

    @Test
    void canSendRemoveNotification() {
        //given
        var notification = new Notification("todoId", "description", new Date());

        // when
        underTest.removeNotification(notification);

        // then
        verify(notificationRepository, times(1)).delete(notification);
    }

    @Test
    void shouldThrowExceptionIfRabbitMQConnectionRefusedForDeleteNotification() {
        //given
        var notification = new Notification("todoId", "description", new Date());

        // when
        willThrow(new AmqpException("")).given(rabbitTemplate).convertAndSend("exchange", "route", notification.toString());
        underTest.removeNotification(notification);

        // then
        verify(notificationRepository, never()).delete(any());

    }
}
