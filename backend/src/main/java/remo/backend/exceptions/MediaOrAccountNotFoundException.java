package remo.backend.exceptions;

public class MediaOrAccountNotFoundException extends RuntimeException {
    public MediaOrAccountNotFoundException(String message) {
        super(message);
    }
}
