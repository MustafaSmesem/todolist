package com.comodo.todolistspring.service;

import com.comodo.todolistspring.document.Role;
import com.comodo.todolistspring.document.User;
import com.comodo.todolistspring.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppUserDetailsServiceTest {

    @Mock private UserRepository userRepository;
    private AppUserDetailsService underTest;

    @BeforeEach
    void setUp() {
        underTest = new AppUserDetailsService(userRepository);
    }

    @Test
    void loadUserByUsername() {

    }

    @Test
    void canLoadUserByUsername() {
        //given
        var userId = "user-id";
        var userName = "MustafaUser";
        var roles = Arrays.asList(new Role("ADMIN_USER"), new Role("STANDARD_USER"));
        var user = new User(userId, userName);
        user.setRoles(roles);
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        //when user exist
        when(userRepository.findUserByUsername(userName)).thenReturn(Optional.of(user));

        //then
        assertThat(underTest.loadUserByUsername(userName).getAuthorities()).isEqualTo(authorities);
    }

    @Test
    void itShouldThrowExceptionIfUserDoesNotExist() {
        //given
        var userName = "MustafaUser";

        //when
        when(userRepository.findUserByUsername(userName)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> underTest.loadUserByUsername(userName))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining(String.format("The username %s doesn't exist", userName));
    }
}
