package com.comodo.todolistspring.repository;

import com.comodo.todolistspring.document.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataMongoTest
class UserRepositoryTest {

    @Autowired private UserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldGetUserById() {
        // given
        var user = new User(null, "MustafaSamism");
        user = underTest.save(user);

        // when
        var expected = underTest.findUserById(user.getId()).get();

        // then
        assertThat(expected).isEqualTo(user);
    }

    @Test
    void itShouldGetUserByUsername() {
        // given
        var username = "MustafaSamism";
        var user = new User(null, username);
        user = underTest.save(user);

        // when
        var expected = underTest.findUserByUsername(username).get();

        // then
        assertThat(expected).isEqualTo(user);
    }

    @Test
    void itShouldCheckIfUserExistByUsername() {
        // given
        var username = "MustafaSamism";
        var user = new User(null, username);
        underTest.save(user);

        // when
        var expected = underTest.existsByUsername(username);

        // then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldGetAllEnabledUsers() {
        // given
        var enabledUsers = createUsers(5, true);
        createUsers(3, false);

        // when
        var expectedEnabled = underTest.findByEnabledTrue();

        // then
        assertThat(expectedEnabled).isEqualTo(enabledUsers);

    }

    private List<User> createUsers(int size, boolean enabled) {
        var users = new ArrayList<User>();
        for (int i=0; i< size; i++) {
            var user = new User(null, "user-" + i);
            user.setEnabled(enabled);
            user = underTest.save(user);
            users.add(user);
        }
        return users;
    }

}
