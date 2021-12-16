package com.comodo.todolistspring.service;

import com.comodo.todolistspring.document.Notification;
import com.comodo.todolistspring.document.Todo;
import com.comodo.todolistspring.document.User;
import com.comodo.todolistspring.exception.BadRequestException;
import com.comodo.todolistspring.exception.DocumentNotFoundException;
import com.comodo.todolistspring.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final NotificationQueue notificationQueue;

    public Todo save(Todo todo, String userId) {
        boolean notification = true;
        if (todo.getId() != null && !todo.getId().isEmpty()) {
            var todoOptional = todoRepository.findById(todo.getId());
            if (todoOptional.isEmpty())
                throw new DocumentNotFoundException("Todo", todo.getId());
            var oldTodo = todoOptional.get();
            if (!oldTodo.getUser().getId().equals(userId))
                throw new BadRequestException("You dont have the permission to modify another person todos");

            notification = (!oldTodo.getDescription().equals(todo.getDescription()) || !oldTodo.getDueDate().equals(todo.getDueDate()));
        }
        todo.setUser(new User(userId));
        todo = todoRepository.save(todo);
        if (notification) notificationQueue.addNotification(new Notification(todo));
        return todo;
    }


    public void deleteTodo(String todoId, String userId) {
        var todoOptional = todoRepository.findById(todoId);
        if (todoOptional.isEmpty())
            throw new DocumentNotFoundException("Todo", todoId);
        var todo = todoOptional.get();
        if (!todo.getUser().getId().equals(userId))
            throw new BadRequestException("You dont have the permission to delete another person todos");
        notificationQueue.removeNotification(new Notification(todo));
        todoRepository.delete(todo);
    }


    public List<Todo> getAll(String userId) {
        return todoRepository.findAllByUserId(userId);
    }


}
