package com.comodo.todolistspring.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<?> handleAllExceptions(Exception ex, WebRequest request) {
        var exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public final ResponseEntity<?> handleDocumentNotFoundException(DocumentNotFoundException ex, WebRequest request) {
        var exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnAuthorizedRequestException.class)
    public final ResponseEntity<?> handleUnAuthorizedRequestException(UnAuthorizedRequestException ex, WebRequest request) {
        var exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(true));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<?> handleBadRequestException(BadRequestException ex, WebRequest request) {
        var exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var exceptionResponse = new ExceptionResponse(new Date(), "Validation failed", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
