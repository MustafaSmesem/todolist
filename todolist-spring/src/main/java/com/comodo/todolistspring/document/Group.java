package com.comodo.todolistspring.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Size;

@Document
@Data
public class Group {

    @Id
    private String id;

    @Size(min = 2, message = "Group title should have at least 2 characters")
    private String title;

    private int todosCount = 0;

    @DBRef
    @JsonIgnore
    @Indexed
    private User user;
}
