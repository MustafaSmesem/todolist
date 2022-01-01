package com.comodo.todolistspring.exception;

import com.comodo.todolistspring.logging.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(String documentName, String id) {
        super(String.format("Cannot found the document(%s) in database: %s", documentName, id));
        Log.warn(String.format("Cannot found the document(%s) in database: %s", documentName, id));
    }
}
