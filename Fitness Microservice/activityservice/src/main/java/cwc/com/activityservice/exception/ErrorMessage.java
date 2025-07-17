package cwc.com.activityservice.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorMessage {
    private String message;
    private String details;
    private String timestamp;
    private int statusCode;
    private String errorType;
    private String path;
}
