package cwc.com.userservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import cwc.com.userservice.model.UserRole;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole userRole;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm a")
    LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm a")
    LocalDateTime updatedAt;
    private List<ActivityResponse> activities;

}
