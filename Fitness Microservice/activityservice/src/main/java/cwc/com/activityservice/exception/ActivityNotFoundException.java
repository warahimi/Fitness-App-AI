package cwc.com.activityservice.exception;

public class ActivityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ActivityNotFoundException(String message) {
        super(message);
    }

    public ActivityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
