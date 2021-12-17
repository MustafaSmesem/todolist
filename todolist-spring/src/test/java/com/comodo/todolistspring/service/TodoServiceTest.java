package com.comodo.todolistspring.service;

import com.comodo.todolistspring.document.Todo;
import com.comodo.todolistspring.document.User;
import com.comodo.todolistspring.exception.BadRequestException;
import com.comodo.todolistspring.exception.DocumentNotFoundException;
import com.comodo.todolistspring.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock private TodoRepository todoRepository;
    @Mock private NotificationQueue notificationQueue;
    private TodoService underTest;

    @BeforeEach
    void setUp() {
        underTest = new TodoService(todoRepository, notificationQueue);
    }

    @Test
    void canSaveNewTodo() {
        // given
        var userId = "user-id";
        var todo = new Todo();
        todo.setUser(new User(userId));
        todo.setDescription("todo description");
        todo.setDueDate(new Date());

        // when
        when(todoRepository.save(todo)).thenReturn(todo);
        todo = underTest.save(todo, userId);

        // then
        var todoArgumentCaptor = ArgumentCaptor.forClass(Todo.class);
        verify(todoRepository).save(todoArgumentCaptor.capture());
        var capturedTodo = todoArgumentCaptor.getValue();
        assertThat(capturedTodo).isEqualTo(todo);
    }

    @Test
    void canUpdateTodo() {
        // given
        var userId = "user-id";
        var todoId = "978465132";
        var todo = new Todo();
        todo.setUser(new User(userId));
        todo.setId(todoId);
        todo.setDescription("todo description");
        todo.setDueDate(new Date());

        // when
        when(todoRepository.findById(todo.getId())).thenReturn(Optional.of(todo));
        underTest.save(todo, userId);

        // then
        verify(todoRepository).save(todo);
    }

    @Test
    void shouldThrowExceptionIfTodoNotFound() {
        // given
        var userId = "user-id";
        var todoId = "978465132";
        var todo = new Todo();
        todo.setUser(new User(userId));
        todo.setId(todoId);

        // when
        when(todoRepository.findById(todo.getId())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.save(todo, userId))
                .isInstanceOf(DocumentNotFoundException.class)
                .hasMessageContaining(String.format("Cannot found the document(Todo) in database: %s", todoId));

    }

    @Test
    void shouldThrowExceptionIfTodoUserNotEqualToAuthenticatedUserId() {
        // given
        var userId = "user-id";
        var anotherUserId = "user-id-another";
        var todoId = "978465132";
        var todo = new Todo();
        todo.setUser(new User(userId));
        todo.setId(todoId);

        // when
        when(todoRepository.findById(todo.getId())).thenReturn(Optional.of(todo));

        // then
        assertThatThrownBy(() -> underTest.save(todo, anotherUserId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("You dont have the permission to modify another person todos");

    }

    @Test
    void canDeleteTodo() {
        //given
        var todoId = "todo-id";
        var userId = "user-id";

        var todo = new Todo();
        todo.setId(todoId);
        todo.setUser(new User(userId));

        //when
        when(todoRepository.findById(todoId)).thenReturn(Optional.of(todo));
        underTest.deleteTodo(todoId, userId);

        //then
        verify(todoRepository).delete(todo);
    }

    @Test
    void itShouldThrowExceptionIfTodoDoesNotExist() {
        //given
        var todoId = "todo-id";
        var userId = "user-id";

        var todo = new Todo();
        todo.setId(todoId);
        todo.setUser(new User(userId));

        //when
        when(todoRepository.findById(todoId)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> underTest.deleteTodo(todoId, userId))
                .isInstanceOf(DocumentNotFoundException.class)
                .hasMessageContaining(String.format("Cannot found the document(Todo) in database: %s", todoId));

    }

    @Test
    void itShouldThrowExceptionIfTodoUserIdNotEqualToAuthenticatedUserId() {
        //given
        var todoId = "todo-id";
        var userId = "user-id";
        var wrongUserId = "wrong-user-id";
        var todo = new Todo();
        todo.setId(todoId);
        todo.setUser(new User(userId));

        //when
        when(todoRepository.findById(todoId)).thenReturn(Optional.of(todo));

        //then
        assertThatThrownBy(() -> underTest.deleteTodo(todoId, wrongUserId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("You dont have the permission to delete another person todos");

    }

    @Test
    void canGetAllTodosByUserId() {
        // when
        var userId = "testUserId";
        underTest.getAll(userId);
        // then
        verify(todoRepository).findAllByUserId(userId);
    }
}
