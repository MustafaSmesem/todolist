package com.comodo.todolistspring.config.security;

import com.comodo.todolistspring.document.records.JwtBadResponse;
import com.comodo.todolistspring.document.records.JwtResponse;
import com.comodo.todolistspring.service.AppUserDetailsService;
import com.comodo.todolistspring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final AppUserDetailsService appUserDetailsService;
    private final UserService userService;


    public ResponseEntity<?> authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            final var userDetails = appUserDetailsService.loadUserByUsername(username);
            final String token = jwtTokenUtil.generateToken(userDetails, 500);

            var user = userService.getUserByUserName(username);
            user.setLastInteractionTime(new Date());
            user.setLoginCount(user.getLoginCount() != null ? user.getLoginCount() + 1 : 1);
            userService.saveUser(user);

            return ResponseEntity.ok(new JwtResponse(userDetails.getId(), username, userDetails.getFullName(), token, userDetails.isAdmin()));
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body(new JwtBadResponse("user_disabled"));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(new JwtBadResponse("user doesn't exist"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(new JwtBadResponse("bad_credentials"));
        }
    }
}
