package com.comodo.todolistspring.controller;

import com.comodo.todolistspring.document.Role;
import com.comodo.todolistspring.document.User;
import com.comodo.todolistspring.document.records.JwtRequest;
import com.comodo.todolistspring.document.records.JwtResponse;
import com.comodo.todolistspring.service.RoleService;
import com.comodo.todolistspring.service.UserService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserService userService;
    @Autowired private RoleService roleService;

    @Test
    void canRegisterAsAdminAndGetAllUsers() throws Exception {
        // create standard role
        var roleS = new Role("STANDARD_USER");
        roleService.save(roleS);

        // create admin role
        var roleA = new Role("ADMIN_USER");
        roleService.save(roleA);

        // given Admin user
        var user = new User();
        user.setName("Mustafa");
        user.setUsername("msamism");
        user.setPassword("1234");
        user.setAdmin(true);

        //register user
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(user))));

        //given
        var authRequest = new JwtRequest(user.getUsername(), user.getPassword());

        // authenticate
        var authResponse = mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(authRequest))))
                .andExpect(status().isOk())
                .andReturn();

        var jwtResponse = jsonToObject(authResponse.getResponse().getContentAsString(), JwtResponse.class);
        assert jwtResponse != null;
        assertThat(user.getUsername().equals(jwtResponse.username())).isTrue();
    }

    @Test
    void register() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void getUser() {
    }


    private String objectToJson(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail("Failed to convert object to json");
            return null;
        }
    }


    private <T> T jsonToObject(String str, Class<T> cls) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(str, cls);
        } catch (JsonProcessingException e) {
            fail("Failed to convert object to json");
            return null;
        }
    }
}
