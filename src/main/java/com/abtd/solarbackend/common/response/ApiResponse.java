package com.abtd.solarbackend.common.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApiResponse<T> {
    private LocalDateTime timestamp;

    private int status;

    private String message;

    private T data;
}
