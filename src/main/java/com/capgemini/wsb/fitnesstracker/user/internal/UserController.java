package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers().stream()
                .map(user -> new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getDateOfBirth(), user.getEmail()))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return userService.getUser(id)
                .map(user -> ResponseEntity.ok(new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getDateOfBirth(), user.getEmail())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.getUser(id).isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userService.getUser(id)
                .map(user -> {
                    user.setFirstName(userDetails.getFirstName());
                    user.setLastName(userDetails.getLastName());
                    user.setDateOfBirth(userDetails.getDateOfBirth());
                    user.setEmail(userDetails.getEmail());
                    return ResponseEntity.ok(userService.updateUser(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<UserDto> searchUsersByEmail(@RequestParam String email) {
        return userService.searchUsersByEmail(email).stream()
                .map(user -> new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getDateOfBirth(), user.getEmail()))
                .toList();
    }

    @GetMapping("/age")
    public List<UserDto> getUsersOlderThan(@RequestParam int age) {
        LocalDate date = LocalDate.now().minusYears(age);
        return userService.findUsersOlderThan(date).stream()
                .map(user -> new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getDateOfBirth(), user.getEmail()))
                .toList();
    }
}
