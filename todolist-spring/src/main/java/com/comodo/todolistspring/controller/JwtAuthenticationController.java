package com.comodo.todolistspring.controller;


import com.comodo.todolistspring.document.records.JwtRequest;
import com.comodo.todolistspring.config.security.JwtService;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    private final JwtService jwtService;
    private final MessageSource messageSource;

    public JwtAuthenticationController(JwtService jwtService, MessageSource messageSource) {
        this.jwtService = jwtService;
        this.messageSource = messageSource;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> authenticateUser(@RequestBody JwtRequest authenticationRequest) {
        return jwtService.authenticate(authenticationRequest.username(), authenticationRequest.password());
    }

    @GetMapping("/hello")
    public String helloInternationalized(@RequestHeader(value = "Accept-language", required = false) String localeString) {
        return messageSource.getMessage("hello.world", null, new Locale(localeString));
    }
}
