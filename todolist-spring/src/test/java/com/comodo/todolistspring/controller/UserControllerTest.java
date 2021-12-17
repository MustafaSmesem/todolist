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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserService userService;
    @Autowired private RoleService roleService;
    @Autowired private MongoTemplate mongoTemplate;


    private JwtResponse jwtResponse;
    private User admin;


    @BeforeEach
    void setUp() throws Exception {
        // create roles
        createRoles();
        // create Admin user
        this.admin = createUser(true, "msamism");

        //register user
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(this.admin))))
                .andExpect(status().isOk());

        //given
        var authRequest = new JwtRequest(this.admin.getUsername(), this.admin.getPassword());

        // authenticate
        var authResponse = mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(authRequest))))
                .andExpect(status().isOk())
                .andReturn();

        this.jwtResponse = jsonToObject(authResponse.getResponse().getContentAsString(), JwtResponse.class);
        assert this.jwtResponse != null;
    }

    @AfterEach
    void tearDown() {
        mongoTemplate.getDb().drop();
    }

    @Test
    void canRegisterAsAdmin() {
        assertThat(this.admin.getUsername().equals(this.jwtResponse.username())).isTrue();
    }

    @Test
    void canGetAllUsersAsAdmin() throws Exception {
        // send getAll Request
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + this.jwtResponse.token());
        mockMvc.perform(get("/users/getAll")
                        .headers(httpHeaders)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
    }

    @Test
    void canUpdateUsersAsAdmin() throws Exception {
        // create standard user
        var userS = createUser(false, "Ssamism");

        //register admin user
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(userS))));

        // send update user Request
        var newName = "Omer";
        var dbUser = userService.getUserByUserName(userS.getUsername());
        dbUser.setName(newName);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + this.jwtResponse.token());
        mockMvc.perform(post("/users/update")
                        .headers(httpHeaders)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(dbUser))))
                    .andExpect(status().isOk());

        assertThat(userService.getUserById(dbUser.getId()).getName()).isEqualTo(newName);
    }

    @Test
    void canCatchErrorsWhenTryToUpdateUser() throws Exception {
        // create standard user
        var userS = createUser(false, "Ssamism");

        //register admin user
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(userS))));

        // send update user Request for user without id
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + this.jwtResponse.token());
        mockMvc.perform(post("/users/update")
                        .headers(httpHeaders)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(userS))))
                    .andExpect(status().isBadRequest());

    }

    @Test
    void canGetUserById() throws Exception {
        // create standard user
        var userS = createUser(false, "Ssamism");


        //register standard user
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(userS))));


        // getUser from db and remove password
        var expectedUser = userService.getUserByUserName(userS.getUsername());
        expectedUser.setPassword("****");

        //get user by id
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + this.jwtResponse.token());
        var result = mockMvc.perform(get("/users/get/" + expectedUser.getId())
                        .headers(httpHeaders)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

        var resultUser = jsonToObject(result.getResponse().getContentAsString(), User.class);
        assertThat(resultUser).isEqualTo(expectedUser);
    }

    private void createRoles() {
        roleService.save(new Role("STANDARD_USER"));
        roleService.save(new Role("ADMIN_USER"));
    }
    private User createUser(boolean isAdmin, String username) {
        var user = new User();
        user.setName("Mustafa");
        user.setUsername(username);
        user.setPassword("1234");
        user.setAdmin(isAdmin);
        return user;
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
