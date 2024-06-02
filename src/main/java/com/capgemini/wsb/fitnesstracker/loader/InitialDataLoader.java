package com.capgemini.wsb.fitnesstracker;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.internal.TrainingRepository;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.internal.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class InitialDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TrainingRepository trainingRepository;

    @Override
    public void run(String... args) {
        User user1 = new User("John", "Doe", LocalDate.of(1990, 1, 1), "john.doe@example.com");
        User user2 = new User("Jane", "Smith", LocalDate.of(1985, 2, 15), "jane.smith@example.com");

        userRepository.saveAll(Arrays.asList(user1, user2));

        Training training1 = new Training(
                LocalDateTime.of(2024, 5, 19, 19, 0),
                LocalDateTime.of(2024, 5, 19, 20, 30),
                "RUNNING",
                14.0,
                user1
        );

        Training training2 = new Training(
                LocalDateTime.of(2024, 5, 17, 19, 0),
                LocalDateTime.of(2024, 5, 17, 20, 30),
                "RUNNING",
                14.0,
                user1
        );

        Training training3 = new Training(
                LocalDateTime.of(2024, 5, 18, 19, 0),
                LocalDateTime.of(2024, 5, 18, 20, 30),
                "TENNIS",
                10.0,
                user2
        );

        trainingRepository.saveAll(Arrays.asList(training1, training2, training3));
    }
}
