package com.comodo.todolistspring.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String id;

    @Indexed
    private String username;

    private String name;
    private String surname;
    private String password;

    @Indexed
    private boolean enabled = true;

    private Date lastInteractionTime;
    private Date creationTime = new Date();
    private boolean isAdmin = false;

    private Integer loginCount = 0;

    @DBRef
    private List<Role> roles;

    public User(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public User(String id) {
        this.id = id;
    }

}
