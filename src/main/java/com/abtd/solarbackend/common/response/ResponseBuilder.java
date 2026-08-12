package com.abtd.solarbackend.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ResponseBuilder {

    private ResponseBuilder() {
    }

    public static <T> ApiResponse<T> success(
            int status,
            String message,
            T data) {

        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(
            String message,
            T data) {

        return ResponseEntity.ok(
                success(HttpStatus.OK.value(), message, data)
        );
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(
            String message,
            T data) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(success(HttpStatus.CREATED.value(), message, data));
    }

}