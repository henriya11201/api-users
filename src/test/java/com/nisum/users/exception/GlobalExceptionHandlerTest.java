package com.nisum.users.exception;

import com.nisum.users.controller.UserController;
import com.nisum.users.model.ApiError;
import com.nisum.users.model.UserRq;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ValidationException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void handleValidationExceptionTest() throws NoSuchMethodException {

        Object target = new UserRq();
        BindingResult bindingResult = new BeanPropertyBindingResult(target, "user");;
        bindingResult.addError(new FieldError("user", "email", "Email is invalid."));

        Method method = UserController.class.getMethod("createUser", UserRq.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);

        ResponseEntity<ApiError> response = globalExceptionHandler.handleValidationException(new MethodArgumentNotValidException(methodParameter, bindingResult));
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Email is invalid.",response.getBody().getDetail());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getBody().getStatus());
    }

    @Test
    void handleConstraintViolationExceptionTest() {

        ResponseEntity<ApiError> response = globalExceptionHandler.handleConstraintViolationException(new ConstraintViolationException(null,null,null));
        Assertions.assertNotNull(response);
        Assertions.assertEquals("El correo ya se encuentra registrado",response.getBody().getDetail());
        Assertions.assertEquals(HttpStatus.CONFLICT.value(),response.getBody().getStatus());
    }

    @Test
    void handleValidationExceptionExceptionTest() {

        ResponseEntity<ApiError> response = globalExceptionHandler.handleValidationException(new ValidationException("Invalid format of password"));
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Invalid format of password",response.getBody().getDetail());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getBody().getStatus());
    }

    @Test
    void handleNoSuchElementExceptionTest() {

        ResponseEntity<ApiError> response = globalExceptionHandler.handleNoSuchElementException(new NoSuchElementException("User not found"));
        Assertions.assertNotNull(response);
        Assertions.assertEquals("User not found",response.getBody().getDetail());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),response.getBody().getStatus());
    }

    @Test
    void handleResponseStatusExceptionTest() {

        ResponseEntity<ApiError> response = globalExceptionHandler.handleResponseStatusException(new ResponseStatusException(HttpStatus.FORBIDDEN,"Token not found"));
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(),response.getBody().getStatus());
    }

    @Test
    void handleUnsupportedOperationExceptionTest() {

        ResponseEntity<ApiError> response = globalExceptionHandler.handleUnsupportedOperationException(new UnsupportedOperationException("Email cannot be updated."));
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.CONFLICT.value(),response.getBody().getStatus());
    }

}
