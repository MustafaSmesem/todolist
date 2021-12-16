package com.comodo.todolistspring.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    private String id;
    private String description;
    private Date dueDate;


    public Notification(Todo todo) {
        this.id = todo.getId();
        this.description = todo.getDescription();
        this.dueDate = todo.getDueDate();
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", dueDate=" + dueDate +
                '}';
    }
}
