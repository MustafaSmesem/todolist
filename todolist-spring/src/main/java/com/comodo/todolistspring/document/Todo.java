package com.comodo.todolistspring.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
public class Todo {

    @Id
    private String id;

    /**
     * false: not finished
     * true: done
     **/
    private boolean state;
    private String description;

    private Date creationDate = new Date();
    private Date dueDate;
    private Date completionDate;

    /**
     *  1 - 4
     **/
    private int priority;


    private String group;

    @DBRef
    @JsonIgnore
    @Indexed
    private User user;
}
