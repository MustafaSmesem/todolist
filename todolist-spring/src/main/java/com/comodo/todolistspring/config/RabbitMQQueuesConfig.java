package com.comodo.todolistspring.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQQueuesConfig {

    @Value("${rabbitmq.notification.exchange:todolist.notification.exchange}")
    private String notificationExchange;

    @Value("${rabbitmq.notification.queue.add:todolist.notification.add.queue}")
    private String notificationAddQueue;
    @Value("${rabbitmq.notification.queue.remove:todolist.notification.remove.queue}")
    private String notificationRemoveQueue;

    @Value("${rabbitmq.notification.route.add:todolist.notification.add.route.#}")
    private String notificationAddRoute;
    @Value("${rabbitmq.notification.route.remove:todolist.notification.remove.route.#}")
    private String notificationRemoveRoute;


    @Bean
    DirectExchange notificationExchangeBean() {
        return new DirectExchange(notificationExchange);
    }

    @Bean
    Queue notificationAddQueueBean() {
        return new Queue(notificationAddQueue, false);
    }
    @Bean
    Queue notificationRemoveQueueBean() {
        return new Queue(notificationRemoveQueue, false);
    }


    @Bean
    public Binding bindingNotificationAddQueue(DirectExchange notificationExchangeBean, Queue notificationAddQueueBean) {
        return BindingBuilder.bind(notificationAddQueueBean).to(notificationExchangeBean).with(notificationAddRoute);
    }
    @Bean
    public Binding bindingNotificationRemoveQueue(DirectExchange notificationExchangeBean, Queue notificationRemoveQueueBean) {
        return BindingBuilder.bind(notificationRemoveQueueBean).to(notificationExchangeBean).with(notificationRemoveRoute);
    }

}
