package com.innowise.Authentication_Service.exception;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private final OffsetDateTime timestamp;

    private final int status;
    private final String error;
    private final String message;
    private final String path;

    // field -> message (для валидации)
    private final Map<String, String> validationErrors;

    private ErrorResponse(
            OffsetDateTime timestamp,
            int status,
            String error,
            String message,
            String path,
            Map<String, String> validationErrors
    ) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.validationErrors = validationErrors;
    }

    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(OffsetDateTime.now(), status, error, message, path, null);
    }

    public static ErrorResponse of(int status, String error, String message, String path, Map<String, String> validationErrors) {
        return new ErrorResponse(OffsetDateTime.now(), status, error, message, path, validationErrors);
    }
}
