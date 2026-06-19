package remo.backend.exceptions;

public class MediaOrAccountNotFoundException extends RuntimeException {
    // TODO: seperate this
    public MediaOrAccountNotFoundException(String message) {
        super(message);
    }
}
