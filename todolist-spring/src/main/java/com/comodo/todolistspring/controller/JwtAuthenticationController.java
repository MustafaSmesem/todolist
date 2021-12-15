package com.comodo.todolistspring.controller;


import com.comodo.todolistspring.document.records.JwtRequest;
import com.comodo.todolistspring.config.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    private final JwtService jwtService;

    public JwtAuthenticationController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> generateAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        return jwtService.authenticate(authenticationRequest.username(), authenticationRequest.password());
    }
}
