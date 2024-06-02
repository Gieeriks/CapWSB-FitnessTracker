package com.capgemini.wsb.fitnesstracker.user.api;

import java.util.Optional;

public interface UserProvider {
    Optional<User> getUserByEmail(String email);
}
