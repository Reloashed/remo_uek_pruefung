package remo.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import remo.backend.dto.ApiError;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<ApiError> handleProfileNotFound(ProfileNotFoundException ex) {
        ApiError error = new ApiError(
                Instant.now(),
                404,
                "PROFILE_NOT_FOUND",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidProfileStateException.class)
    public ResponseEntity<ApiError> handleInvalidProfileState(InvalidProfileStateException ex) {
        ApiError error = new ApiError(
                Instant.now(),
                400,
                "INVALID_PROFILE_STATE",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MediaOrAccountNotFoundException.class)
    public ResponseEntity<ApiError> handleMediaOrAccountNotFound(MediaOrAccountNotFoundException ex) {
        ApiError error = new ApiError(
                Instant.now(),
                404,
                "MEDIA_OR_ACCOUNT_NOT_FOUND",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
