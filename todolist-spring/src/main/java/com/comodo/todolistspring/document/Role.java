
package com.comodo.todolistspring.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    private String name;
    private String description;

    public Role(String name) {
        this.name = name;
    }
}
