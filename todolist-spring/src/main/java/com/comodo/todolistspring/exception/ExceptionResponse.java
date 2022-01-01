package com.comodo.todolistspring.exception;
import java.util.Date;

public record ExceptionResponse(Date timestamp, String message, String details) {
}
