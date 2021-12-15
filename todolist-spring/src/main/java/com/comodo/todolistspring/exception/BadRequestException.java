package com.comodo.todolistspring.exception;

import com.comodo.todolistspring.logging.Log;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String msg) {
        super(msg);
        Log.error(msg);
    }
}
