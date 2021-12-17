package com.comodo.todolistspring.controller;

import com.comodo.todolistspring.document.Role;
import com.comodo.todolistspring.document.Group;
import com.comodo.todolistspring.document.User;
import com.comodo.todolistspring.document.records.JwtRequest;
import com.comodo.todolistspring.document.records.JwtResponse;
import com.comodo.todolistspring.service.RoleService;
import com.comodo.todolistspring.service.GroupService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GroupControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private MongoTemplate mongoTemplate;
    @Autowired private RoleService roleService;


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
    void canSaveNewGroup() throws Exception {
        //given
        var group = new Group();

        // send save Request
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + this.jwtResponse.token());
        mockMvc.perform(post("/group/save")
                        .headers(httpHeaders)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(group))))
                .andExpect(status().isOk());


    }

    @Test
    void itShouldCatchUnAuthorizedExceptionWhenTryToUpdate() throws Exception {
        //given
        var group = new Group();
        group.setTitle("work");
        group.setUser(admin);

        // set headers
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + this.jwtResponse.token());

        // send save Request
        var result = mockMvc.perform(post("/group/save")
                        .headers(httpHeaders)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(group))))
                .andExpect(status().isOk())
                .andReturn();

        var resultGroup = jsonToObject(result.getResponse().getContentAsString(), Group.class);
        assert resultGroup !=null;


        // login with another user
        var anotherUser = createUser(true, "usamism");
        //register user
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(anotherUser))));

        //given
        var authRequest = new JwtRequest(anotherUser.getUsername(), anotherUser.getPassword());

        // authenticate
        var authResponse = mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(authRequest))))
                .andReturn();

        var response = jsonToObject(authResponse.getResponse().getContentAsString(), JwtResponse.class);
        assert response != null;


        httpHeaders.set("Authorization", "Bearer " + response.token());
        // send update Request
        mockMvc.perform(post("/group/save")
                        .headers(httpHeaders)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(resultGroup))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void itShouldCatchNotFoundExceptionWhenTryToUpdate() throws Exception {
        //given
        var group = new Group();
        group.setTitle("work");
        group.setUser(admin);
        group.setId("sacma-id");

        // set headers
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + this.jwtResponse.token());


        // send update Request
        mockMvc.perform(post("/group/save")
                        .headers(httpHeaders)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(group))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void canDeleteGroup() throws Exception {
        //given
        var group = new Group();
        group.setTitle("work");
        group.setUser(admin);

        // send save Request
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + this.jwtResponse.token());
        var result = mockMvc.perform(post("/group/save")
                        .headers(httpHeaders)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(group))))
                .andExpect(status().isOk())
                .andReturn();

        var groupId = jsonToObject(result.getResponse().getContentAsString(), Group.class).getId();

        // send remove request
        mockMvc.perform(delete("/group/" + groupId)
                        .headers(httpHeaders)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }



    @Test
    void itShouldCatchUnAuthorizedExceptionWhenTryingDelete() throws Exception {
        //given
        var group = new Group();
        group.setTitle("work");
        group.setUser(admin);

        // set headers
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + this.jwtResponse.token());

        // send save Request
        var result = mockMvc.perform(post("/group/save")
                        .headers(httpHeaders)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(group))))
                .andExpect(status().isOk())
                .andReturn();

        var resultGroup = jsonToObject(result.getResponse().getContentAsString(), Group.class);
        assert resultGroup !=null;


        // login with another user
        var anotherUser = createUser(true, "usamism");
        //register user
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(anotherUser))));

        //given
        var authRequest = new JwtRequest(anotherUser.getUsername(), anotherUser.getPassword());

        // authenticate
        var authResponse = mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(authRequest))))
                .andReturn();

        var response = jsonToObject(authResponse.getResponse().getContentAsString(), JwtResponse.class);
        assert response != null;


        httpHeaders.set("Authorization", "Bearer " + response.token());
        // send update Request
        mockMvc.perform(delete("/group/" + resultGroup.getId())
                        .headers(httpHeaders)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void itShouldCatchNotFoundExceptionWhenTryingDelete() throws Exception {
        // set headers
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + this.jwtResponse.token());


        // send remove request
        mockMvc.perform(delete("/group/anyId")
                        .headers(httpHeaders)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void canGetAllGroupsById() throws Exception {
        // set headers
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + this.jwtResponse.token());

        // send getAll request
        mockMvc.perform(get("/group/all")
                        .headers(httpHeaders)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

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
