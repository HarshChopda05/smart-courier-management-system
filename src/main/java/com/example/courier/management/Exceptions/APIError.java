package com.example.courier.management.Exceptions;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class APIError {

    private LocalDateTime timeStamp;
    private int status;
    private String error;
    private String message;

    public APIError(LocalDateTime timeStamp, int status, String error, String message) {
        this.timeStamp = timeStamp;
        this.status = status;
        this.error = error;
        this.message = message;
    }

}
