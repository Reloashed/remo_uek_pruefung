package remo.backend.exceptions;

public class NotAllowedToModifyGroup extends RuntimeException {
    public NotAllowedToModifyGroup(String message) {
        super(message);
    }
}
