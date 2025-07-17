package cwc.com.userservice.controller;

import cwc.com.userservice.dto.UserRequest;
import cwc.com.userservice.dto.UserResponse;
import cwc.com.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @GetMapping("/{Id}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String Id)
    {
        return ResponseEntity.ok(userService.getUserById(Id));
    }
    @PostMapping()
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRequest userRequest)
    {
        return ResponseEntity.ok(userService.saveUser(userRequest));
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }
    @PutMapping("/{Id}")
    public ResponseEntity<UserResponse> updateUserProfile(
            @PathVariable String Id,
            @RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity.ok(userService.updateUser(Id, userRequest));
    }
    @GetMapping("/validate/{userId}")
    public ResponseEntity<Boolean> validateUser(@PathVariable String userId) {
        return ResponseEntity.ok(userService.validateUser(userId));
    }



}
