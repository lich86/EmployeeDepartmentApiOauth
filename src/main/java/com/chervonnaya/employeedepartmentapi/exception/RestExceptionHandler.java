package com.chervonnaya.employeedepartmentapi.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@RestControllerAdvice
public class RestExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(final EntityNotFoundException e){
        ErrorResponse response = new ErrorResponse(
            String.format("Unable to get %s with id %d. Message: %s", e.getEntityClass().toLowerCase(),
                e.getId(), e.getMessage()),
            HttpStatus.NOT_FOUND,
            now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(", "));
        ErrorResponse response = new ErrorResponse(
            String.format("Invalid JSON input: %s", errorMessage),
            HttpStatus.BAD_REQUEST,
            now()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException
        (final HttpMessageNotReadableException e){
        String errorMessage;
        if(e.getMessage()
            .contains("Cannot deserialize value of type `com.chervonnaya.orderrestapi.model.enums.Status`")) {
            errorMessage = "Please provide correct status value";
        } else {
            errorMessage = e.getMessage();
        }
        ErrorResponse response = new ErrorResponse(
            String.format("Invalid JSON input: %s", errorMessage),
            HttpStatus.BAD_REQUEST,
            now()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse>
    handleDataIntegrityViolationException(final DataIntegrityViolationException e){
        ErrorResponse response = new ErrorResponse(
            String.format("DataIntegrityViolationException: %s", extractDetailMessage(e.getMessage())),
            HttpStatus.CONFLICT,
            now()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SaveEntityException.class)
    public ResponseEntity<ErrorResponse> handleSaveEntityException(SaveEntityException e) {
        ErrorResponse response = new ErrorResponse(
            String.format("An error occurred while saving the %s: %s", e.getEntityClass().toLowerCase(),
                e.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR,
            now()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception e) {
        ErrorResponse response = new ErrorResponse(
            String.format("An unexpected error occurred: %s", e.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR,
            now()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    public record ErrorResponse(String message, HttpStatus statusCode, LocalDateTime timestamp) {
    }

    private static String extractDetailMessage(String fullMessage) {
        Pattern pattern = Pattern.compile("Detail: Key \\(.*?\\) already exists\\.");
        Matcher matcher = pattern.matcher(fullMessage);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "Duplicate entry detected";
    }
}
