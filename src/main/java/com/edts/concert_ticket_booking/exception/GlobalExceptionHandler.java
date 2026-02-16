package com.edts.concert_ticket_booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.edts.concert_ticket_booking.api.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusiness(BusinessException ex) {

        return ResponseEntity
                .status(ex.getStatus())
                .body(ApiResponse.fail(
                        ex.getErrorCode(),
                        ex.getMessage()));
    }

     @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors()
                           .stream()
                           .map(f -> f.getField() + ": " + f.getDefaultMessage())
                           .findFirst()
                           .orElse("Invalid request");
        ApiResponse<?> response = ApiResponse.fail("BAD_REQUEST", message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}