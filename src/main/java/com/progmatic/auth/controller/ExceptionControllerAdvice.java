package com.progmatic.auth.controller;

import com.progmatic.auth.exception.UserAlreadyExistsException;
import com.progmatic.auth.utils.ErrorResponse;
import com.progmatic.auth.utils.ValidationErrorResponse;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = {"com.progmatic.auth.controller"})
public class ExceptionControllerAdvice {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException methodArgumentNotValidException) {
    Map<String, String> messages = new HashMap<>();
    methodArgumentNotValidException
        .getBindingResult()
        .getFieldErrors()
        .forEach(error -> messages.put(error.getField(), error.getDefaultMessage()));
    log.error(
        "Invalid request body. Following constraints have been violated. {}",
        messages,
        methodArgumentNotValidException);
    ValidationErrorResponse errorResponse =
        new ValidationErrorResponse.Builder()
            .code("VALIDATION_ERRORS")
            .messages(messages)
            .timestamptz(Instant.now())
            .build();
    return ResponseEntity.status(HttpStatusCode.valueOf(422)).body(errorResponse);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ValidationErrorResponse> handleConstraintViolationException(
      ConstraintViolationException constraintViolationException) {
    Map<String, String> messages = new HashMap<>();
    constraintViolationException
        .getConstraintViolations()
        .forEach(
            constraintViolation ->
                messages.put(
                    constraintViolation.getPropertyPath().toString(),
                    constraintViolation.getMessage()));
    log.error(
        "Invalid request body. Following constraints have been violated. {}",
        messages,
        constraintViolationException);
    ValidationErrorResponse errorResponse =
        new ValidationErrorResponse.Builder()
            .code("VALIDATION_ERRORS")
            .messages(messages)
            .timestamptz(Instant.now())
            .build();
    return ResponseEntity.status(HttpStatusCode.valueOf(422)).body(errorResponse);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleAuthException(
      UserAlreadyExistsException userAlreadyExistsException) {
    log.error("User registration failed.", userAlreadyExistsException);
    ErrorResponse errorResponse =
        new ErrorResponse.Builder()
            .message("USER_ALREADY_EXISTS")
            .code(userAlreadyExistsException.getMessage())
            .timestamptz(Instant.now())
            .build();
    return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(errorResponse);
  }
}
