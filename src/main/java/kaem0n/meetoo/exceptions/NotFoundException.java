package kaem0n.meetoo.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(long id) {
        super("Element with ID '" + id + "' not found.");
    }

    public NotFoundException(UUID id) {
        super("Element with ID '" + id + "' not found.");
    }

    public NotFoundException(String msg) {
        super(msg);
    }
}
