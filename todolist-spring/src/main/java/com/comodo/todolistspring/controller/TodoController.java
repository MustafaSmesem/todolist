package com.comodo.todolistspring.controller;

import com.comodo.todolistspring.document.Todo;
import com.comodo.todolistspring.exception.BadRequestException;
import com.comodo.todolistspring.exception.DocumentNotFoundException;
import com.comodo.todolistspring.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping(value = "/save", produces = "application/json")
    public ResponseEntity<?> saveGroup(@RequestAttribute("userId") String userId, @RequestBody Todo formTodo) {
        try{
            var todo = todoService.save(formTodo, userId);
            return ResponseEntity.status(HttpStatus.OK).body(todo);
        } catch (DocumentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deleteGroup(@RequestAttribute("userId") String userId, @PathVariable("id") String todoId) {
        try{
            todoService.deleteTodo(todoId, userId);
            return ResponseEntity.ok(String.format("Todo [%s] has been removed successfully", todoId));
        } catch (DocumentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<?> getAllGroups(@RequestAttribute("userId") String userId) {
        try {
            return ResponseEntity.ok(todoService.getAll(userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
