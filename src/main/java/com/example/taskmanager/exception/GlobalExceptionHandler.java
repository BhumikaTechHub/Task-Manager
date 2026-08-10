package com.example.taskmanager.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ApiErrorResponse buildError(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {

        return new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {

        logger.warn(
                "Resource not found: {} | Path: {}",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                buildError(
                        HttpStatus.NOT_FOUND,
                        ex.getMessage(),
                        request
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex,
            HttpServletRequest request
    ) {

        logger.warn(
                "Registration failed: {} | Path: {}",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                buildError(
                        HttpStatus.CONFLICT,
                        ex.getMessage(),
                        request
                ),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request
    ) {

        logger.warn(
                "Authentication failed for request: {}",
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                buildError(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password",
                        request
                ),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {

        String error = ex.getBindingResult()
                .getFieldError()
                .getDefaultMessage();

        logger.warn(
                "Validation failed: {} | Path: {}",
                error,
                request.getRequestURI()
        );

        return new ResponseEntity<>(
                buildError(
                        HttpStatus.BAD_REQUEST,
                        error,
                        request
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(
            Exception ex,
            HttpServletRequest request
    ) {

        logger.error(
                "Unexpected exception occurred while processing request: {}",
                request.getRequestURI(),
                ex
        );

        return new ResponseEntity<>(
                buildError(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred",
                        request
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}