package com.capgemini.wsb.fitnesstracker.user.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    Optional<User> getUser(Long userId);
    Optional<User> getUserByEmail(String email);
    List<User> findAllUsers();
    void deleteUser(Long userId);
    User updateUser(User user);
    List<User> searchUsersByEmail(String email);
    List<User> findUsersOlderThan(LocalDate date);
}
