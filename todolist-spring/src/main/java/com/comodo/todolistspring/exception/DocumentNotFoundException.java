package com.comodo.todolistspring.exception;

import com.comodo.todolistspring.logging.Log;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(String documentName, String id) {
        super(String.format("Cannot found the document(%s) in database: %s", documentName, id));
        Log.warn(String.format("Cannot found the document(%s) in database: %s", documentName, id));
    }
}
