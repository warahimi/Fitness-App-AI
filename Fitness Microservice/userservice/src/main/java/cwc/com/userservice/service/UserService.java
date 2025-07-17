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
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public UserResponse getUserById(String Id) {
        User user = userRepository.findById(Id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + Id + " not found."));
        return Util.userToUserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(Util::userToUserResponse)
                .toList();
    }
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found."));
        return Util.userToUserResponse(user);
    }

    public UserResponse saveUser(UserRequest userRequest) {
        if(userRepository.existsByEmail(userRequest.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + userRequest.getEmail() + " already exists.");
        }
        User user = Util.userRquestToUser(userRequest);
        User savedUser = userRepository.save(user);
        return Util.userToUserResponse(savedUser);
    }
    public UserResponse updateUser(String Id, UserRequest userRequest) {
        User user = userRepository.findById(Id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + Id + " not found."));

        // Update user fields
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        User updatedUser = userRepository.save(user);
        return Util.userToUserResponse(updatedUser);
    }
    public Boolean validateUser(String userId)
    {
        return userRepository.existsById(userId);
    }
}
