package cwc.com.userservice.exceptions;

import lombok.Getter;

import java.io.Serial;

public class UserNotFoundException extends RuntimeException {
    @Getter
    @Serial
    private static final long serialVersionUID = 1L;

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
