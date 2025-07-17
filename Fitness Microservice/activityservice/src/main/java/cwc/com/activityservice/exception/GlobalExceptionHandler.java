package cwc.com.activityservice.exception;

import jakarta.validation.UnexpectedTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .message(ex.getMessage())
                .details("User not found in the system")
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss a")))
                .statusCode(404)
                .errorType("User Not Found")
                .path("/api/v1/users")
                .build();
        return ResponseEntity.status(404).body(errorMessage);
    }
    @ExceptionHandler(ActivityNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleActivityNotFoundException(ActivityNotFoundException ex) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .message(ex.getMessage())
                .details("Activity not found in the system")
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss a")))
                .statusCode(404)
                .errorType("Activity Not Found")
                .path("/api/v1/activities")
                .build();
        return ResponseEntity.status(404).body(errorMessage);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        errors.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss a")));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body("Invalid value for " + ex.getName() + ": must be a number");
    }
    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<ErrorMessage> handleUnexpectedTypeException(UnexpectedTypeException ex) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .message("Invalid data type provided")
                .details(ex.getMessage())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss a")))
                .statusCode(400)
                .errorType("Bad Request")
                .path("/api/v1/activities")
                .build();
        return ResponseEntity.badRequest().body(errorMessage);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGenericException(Exception ex) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .message("An unexpected error occurred")
                .details(ex.getMessage())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss a")))
                .statusCode(500)
                .errorType("Internal Server Error")
                .path("/api/v1/activities")
                .build();
        return ResponseEntity.status(500).body(errorMessage);
    }
}
