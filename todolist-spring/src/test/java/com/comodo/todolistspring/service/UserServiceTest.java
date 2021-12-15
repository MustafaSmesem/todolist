package com.comodo.todolistspring.service;

import com.comodo.todolistspring.document.User;
import com.comodo.todolistspring.exception.BadRequestException;
import com.comodo.todolistspring.exception.DocumentNotFoundException;
import com.comodo.todolistspring.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository);
    }


    @Test
    void canCreateUser() {
        // given
        var user = new User(null, "MustafaSAMISM");

        // when
        underTest.saveUser(user);

        // then
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        var capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(user);
    }

    @Test
    void itShouldThrowExceptionWhenUsernameIsTaken() {
        // given
        var user = new User(null, "MustafaSAMISM");
        given(userRepository.existsByUsername(anyString())).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> underTest.saveUser(user))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(String.format("this email [%s] is already registered", user.getUsername()));

        verify(userRepository, never()).save(any());
    }

    @Test
    void canGetAllUsers() {
        // when
        underTest.getAll();
        // then
        verify(userRepository).findByEnabledTrue();
    }

    @Test
    void canGetUserByUserName() {
        // given
        var user = new User(null, "MustafaSAMISM");
        given(userRepository.findUserByUsername(user.getUsername())).willReturn(Optional.of(user));

        // when
        // then
        assertThat(underTest.getUserByUserName(user.getUsername()))
                .isInstanceOf(User.class)
                .isEqualTo(user);
    }

    @Test
    void itShouldThrowExceptionIfThereNoUserWithUsername() {
        // given
        var user = new User(null, "MustafaSAMISM");
        given(userRepository.findUserByUsername(user.getUsername())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getUserByUserName(user.getUsername()))
                .isInstanceOf(DocumentNotFoundException.class)
                .hasMessageContaining(String.format("Cannot found the document(User) in database: %s", user.getUsername()));
    }

    @Test
    void canGetUserById() {
        // given
        var user = new User("123456789", "MustafaSAMISM");
        given(userRepository.findUserById(user.getId())).willReturn(Optional.of(user));

        // when
        // then
        assertThat(underTest.getUserById(user.getId()))
                .isInstanceOf(User.class)
                .isEqualTo(user);
    }

    @Test
    void itShouldThrowExceptionIfThereNoUserWithThisId() {
        // given
        var user = new User("123456789", "MustafaSAMISM");
        given(userRepository.findUserById(anyString())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getUserById(user.getId()))
                .isInstanceOf(DocumentNotFoundException.class)
                .hasMessageContaining(String.format("Cannot found the document(User) in database: %s", user.getId()));
    }
}
