package com.comodo.todolistspring.repository;


import com.comodo.todolistspring.document.Todo;
import com.comodo.todolistspring.document.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataMongoTest
class TodoRepositoryTest {

    @Autowired private TodoRepository underTest;
    @Autowired private UserRepository userRepository;

    @Test
    void itShouldGetAllTodosRelatedToUser() {
        // given
        var users = createUsers(3);
        var todos_0 = createTodos(3, users.get(0));
        var todos_1 = createTodos(5, users.get(1));
        var todos_2 = createTodos(0, users.get(2));

        // where
        var expected_todo_0 = underTest.findAllByUserId(users.get(0).getId());
        var expected_todo_1 = underTest.findAllByUserId(users.get(1).getId());
        var expected_todo_2 = underTest.findAllByUserId(users.get(2).getId());

        //then
        assertThat(expected_todo_0).isEqualTo(todos_0);
        assertThat(expected_todo_1).isEqualTo(todos_1);
        assertThat(expected_todo_2).isEqualTo(todos_2);

    }

    private List<User> createUsers(int size) {
        var users = new ArrayList<User>();
        for (int i=0; i< size; i++) {
            var user = new User(null, "user-" + i);
            user = userRepository.save(user);
            users.add(user);
        }
        return users;
    }

    private List<Todo> createTodos(int size, User user) {
        var todos = new ArrayList<Todo>();
        for (int i=0; i< size; i++) {
            var todo = new Todo();
            todo.setUser(user);
            todo.setDescription("Todo-" + user.getUsername() + "_" + i);
            todo = underTest.save(todo);
            todos.add(todo);
        }
        return todos;
    }
}
