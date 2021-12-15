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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BootstrapDB implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MongoTemplate mongoTemplate;
    private final GroupRepository groupRepository;


    @Value("${db.bootstrap}") private boolean isActive;
    @Value("${db.drop}") private boolean dropDB;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (isActive) {
            Log.warn("Bootstrapping db");
            if (dropDB) {
                Log.warn("Drop database before bootstrapping");
                mongoTemplate.getDb().drop();
            }
            var role1 = createRole("STANDARD_USER", "Standard User - Has no admin rights");
            var role2 = createRole("ADMIN_USER", "Admin User - Has permission to perform admin tasks");
            createUser("Mustafa", "SAMISM", "mustafa.smesem@gmail.com", "1234" , true, Arrays.asList(role1, role2));
            createUser("Mustafa", "SAMISM", "user@doamin.com", "1234" , false, List.of(role1));

        }
        Log.warn("Comodo %s backend server has been started", "TodoList");
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

}
