package cwc.com.userservice.service;

import cwc.com.userservice.dto.UserRequest;
import cwc.com.userservice.dto.UserResponse;
import cwc.com.userservice.entity.User;
import cwc.com.userservice.exceptions.UserAlreadyExistsException;
import cwc.com.userservice.exceptions.UserNotFoundException;
import cwc.com.userservice.repository.UserRepository;
import cwc.com.userservice.util.Util;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public UserResponse getUserProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));
        return Util.userToUserResponse(user);
    }

    public UserResponse registerUser(UserRequest userRequest) {
        if(userRepository.existsByEmail(userRequest.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + userRequest.getEmail() + " already exists.");
        }
        User user = Util.userRquestToUser(userRequest);
        user.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm: a")));
        user.setUpdatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm: a")));
        User savedUser = userRepository.save(user);
        return Util.userToUserResponse(savedUser);
    }
    public UserResponse updateUserProfile(String userId, UserRequest userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));

        // Update user fields
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setUpdatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm: a")));

        User updatedUser = userRepository.save(user);
        return Util.userToUserResponse(updatedUser);
    }
}
