package com.nisum.users.exception;

import com.nisum.users.model.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ValidationException;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex) {

        String errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.error("handleValidationExceptions", ex);
        return setResponseEntity(errors, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("handleConstraintViolationException", ex);
        return setResponseEntity("El correo ya se encuentra registrado", HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(ValidationException ex) {
        log.error("handleValidationException", ex);
        return setResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> handleNoSuchElementException(NoSuchElementException ex) {
        log.error("handleNoSuchElementException", ex);
        return setResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatusException(ResponseStatusException ex) {
        log.error("handleResponseStatusException", ex);
        return setResponseEntity(ex.getMessage(), ex.getStatus().value(), ex.getStatus().getReasonPhrase());
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ApiError> handleUnsupportedOperationException(UnsupportedOperationException ex) {
        log.error("handleUnsupportedOperationException", ex);
        return setResponseEntity(ex.getMessage(), HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase());
    }

    private ResponseEntity<ApiError> setResponseEntity(String detail, int value, String message) {

        ApiError apiError = new ApiError();
        apiError.setStatus(value);
        apiError.setDetail(detail);
        apiError.setMessage(message);

        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.status(value).headers(httpHeaders).body(apiError);

    }


}
