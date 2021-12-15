package com.comodo.todolistspring.service;

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

    public Todo save(Todo todo, String userId) {
        if (todo.getId() != null && !todo.getId().isEmpty()) {
            var todoOptional = todoRepository.findById(todo.getId());
            if (todoOptional.isEmpty())
                throw new DocumentNotFoundException("Todo", todo.getId());
            if (!todoOptional.get().getUser().getId().equals(userId))
                throw new BadRequestException("You dont have the permission to modify another person todos");
        }
        todo.setUser(new User(userId));
        return todoRepository.save(todo);
    }


    public void deleteTodo(String todoId, String userId) {
        var todoOptional = todoRepository.findById(todoId);
        if (todoOptional.isEmpty())
            throw new DocumentNotFoundException("Todo", todoId);
        var todo = todoOptional.get();
        if (!todo.getUser().getId().equals(userId))
            throw new BadRequestException("You dont have the permission to delete another person todos");
        todoRepository.delete(todo);
    }


    public List<Todo> getAll(String userId) {
        return todoRepository.findAllByUserId(userId);
    }


}
