package com.comodo.todolistspring.service;

import com.comodo.todolistspring.document.Notification;
import com.comodo.todolistspring.logging.Log;
import com.comodo.todolistspring.repository.NotificationRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationQueue {

    private final RabbitTemplate rabbitTemplate;
    private final NotificationRepository notificationRepository;

    @Value("${rabbitmq.notification.exchange:todolist.notification.exchange}")
    private String notificationExchange;

    @Value("${rabbitmq.notification.route.add:todolist.notification.add.route.#}")
    private String notificationAddRoute;
    @Value("${rabbitmq.notification.route.remove:todolist.notification.remove.route.#}")
    private String notificationRemoveRoute;

    public NotificationQueue(RabbitTemplate rabbitTemplate, NotificationRepository notificationRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.notificationRepository = notificationRepository;
    }


    public void addNotification(Notification notification) {
        try {
            rabbitTemplate.convertAndSend(notificationExchange, notificationAddRoute, notification.toString());
            Log.warn("Notification [%s] send to rabbitMQ broker for add", notification.getDescription());
            notificationRepository.save(notification);
        } catch (Exception e) {
            Log.error("Cannot send notification: %s", e.getMessage());
        }
    }

    public void removeNotification(Notification notification) {
        try {
            rabbitTemplate.convertAndSend(notificationExchange, notificationRemoveRoute, notification.getId());
            Log.warn("Notification [%s] send to rabbitMQ broker for delete", notification.getDescription());
            notificationRepository.delete(notification);
        } catch (Exception e) {
            Log.error("Cannot send notification: %s", e.getMessage());
        }
    }


}
