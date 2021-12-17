package com.comodo.todolistspring.config.security;

import com.comodo.todolistspring.document.User;
import com.comodo.todolistspring.service.AppUserDetailsService;
import com.comodo.todolistspring.service.NotificationQueue;
import com.comodo.todolistspring.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtTokenUtil jwtTokenUtil;
    @Mock private AppUserDetailsService appUserDetailsService;
    @Mock private UserService userService;

    private JwtService underTest;

    @BeforeEach
    void setUp() {
        underTest = new JwtService(authenticationManager, jwtTokenUtil, appUserDetailsService, userService);
    }

    @Test
    void itShouldCatchDisabledUserException() {
        //given
        var username = "username";
        var password = "password";

        //then
        willThrow(new DisabledException("")).given(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(username, password));
        underTest.authenticate(username, password);

        //then
        verify(userService, never()).getUserByUserName(any());

    }

    @Test
    void itShouldBadCredentialsException() {
        //given
        var username = "username";
        var password = "password";

        //then
        willThrow(new BadCredentialsException("")).given(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(username, password));
        underTest.authenticate(username, password);

        //then
        verify(userService, never()).getUserByUserName(any());

    }

    @Test
    void itShouldCatchUsernameNotFoundException() {
        //given
        var username = "username";
        var password = "password";

        //then
        willThrow(new UsernameNotFoundException("")).given(userService).getUserByUserName(username);
        underTest.authenticate(username, password);

        //then
        verify(userService, never()).saveUser(any());

    }
}
