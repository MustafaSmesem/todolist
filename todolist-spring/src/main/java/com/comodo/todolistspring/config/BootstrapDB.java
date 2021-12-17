package com.comodo.todolistspring.config;

import com.comodo.todolistspring.document.Group;
import com.comodo.todolistspring.document.Role;
import com.comodo.todolistspring.document.User;
import com.comodo.todolistspring.logging.Log;
import com.comodo.todolistspring.repository.GroupRepository;
import com.comodo.todolistspring.repository.RoleRepository;
import com.comodo.todolistspring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name= "application.db-init", havingValue = "true")
public class BootstrapDB implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MongoTemplate mongoTemplate;
    private final GroupRepository groupRepository;


    @Value("${application.db-init-checker:/tmp/.initDB}")
    private String checkerPath;

    @Override
    public void run(ApplicationArguments args) {
        if (alreadyInitialized()) {
            Log.warn("Database already initialized, TodoList backend server has been started");
            return;
        }
        Log.warn("Initializing database...");
        mongoTemplate.getDb().drop();
        var role1 = createRole("STANDARD_USER", "Standard User - Has no admin rights");
        var role2 = createRole("ADMIN_USER", "Admin User - Has permission to perform admin tasks");
        createUser("Admin", "USER", "admin@domain.test", "1234" , true, Arrays.asList(role1, role2));
        createUser("Standard", "USER", "user@domain.test", "1234" , false, List.of(role1));
        Log.warn("New users has been added: \nadmin@doamin.test -> 1234\nuser@doamin.test -> 1234");
        Log.warn("TodoList backend server has been started");
    }

    private Role createRole(String name, String description) {
        return roleRepository.save(new Role(name, description));
    }

    private void createUser(String name, String surname, String username, String password, boolean isAdmin, List<Role> roles) {
        var user = new User();
        user.setEnabled(true);
        user.setName(name);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.setSurname(surname);
        user.setUsername(username);
        user.setAdmin(isAdmin);
        user.setRoles(roles);
        user = userRepository.save(user);
        createDefaultGroup(user);
    }

    private void createDefaultGroup(User user) {
        var group = new Group();
        group.setUser(user);
        group.setTitle("Default");
        groupRepository.save(group);
    }

    private boolean alreadyInitialized() {
        try {
            File myObj = new File(checkerPath);
            if (!myObj.createNewFile()) return true;
        } catch (IOException e) {
            Log.error("An error occurred.");
            e.printStackTrace();
        }
        return false;
    }

}
