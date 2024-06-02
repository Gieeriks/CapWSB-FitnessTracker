package com.capgemini.wsb.fitnesstracker.training;

import com.capgemini.wsb.fitnesstracker.IntegrationTest;
import com.capgemini.wsb.fitnesstracker.IntegrationTestBase;
import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@Transactional
@AutoConfigureMockMvc(addFilters = false)
class TrainingApiIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllTrainings_whenGettingAllTrainings() throws Exception {

        User user1 = existingUser(generateClient());
        Training training1 = persistTraining(generateTraining(user1));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        mockMvc.perform(get("/api/trainings").contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].userId").value(user1.getId()))
                .andExpect(jsonPath("$[0].startTime").value(training1.getStartTime().format(formatter)))
                .andExpect(jsonPath("$[0].endTime").value(training1.getEndTime().format(formatter)))
                .andExpect(jsonPath("$[0].distance").value(training1.getDistance()))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void shouldReturnAllTrainingsForDedicatedUser_whenGettingAllTrainingsForDedicatedUser() throws Exception {

        User user1 = existingUser(generateClient());
        Training training1 = persistTraining(generateTraining(user1));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        mockMvc.perform(get("/api/trainings/user/{userId}", user1.getId()).contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].userId").value(user1.getId()))
                .andExpect(jsonPath("$[0].startTime").value(training1.getStartTime().format(formatter)))
                .andExpect(jsonPath("$[0].endTime").value(training1.getEndTime().format(formatter)))
                .andExpect(jsonPath("$[0].distance").value(training1.getDistance()))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void shouldReturnAllFinishedTrainingsAfterTime_whenGettingAllFinishedTrainingsAfterTime() throws Exception {

        User user1 = existingUser(generateClient());
        Training training1 = persistTraining(generateTrainingWithDetails(user1, "2024-05-19T19:00:00", "2024-05-19T20:30:00", "RUNNING", 14.0));
        Training training2 = persistTraining(generateTrainingWithDetails(user1, "2024-05-17T19:00:00", "2024-05-17T20:30:00", "RUNNING", 14.0));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        mockMvc.perform(get("/api/trainings/finished/{afterTime}", "2024-05-18").contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].userId").value(user1.getId()))
                .andExpect(jsonPath("$[0].startTime").value(training1.getStartTime().format(formatter)))
                .andExpect(jsonPath("$[0].endTime").value(training1.getEndTime().format(formatter)))
                .andExpect(jsonPath("$[0].distance").value(training1.getDistance()))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void getAllTrainingByActivityType_whenGettingAllTrainingByActivityType() throws Exception {

        User user1 = existingUser(generateClient());
        Training training1 = persistTraining(generateTrainingWithActivityType(user1, "RUNNING"));
        Training training2 = persistTraining(generateTrainingWithActivityType(user1, "TENNIS"));
        Training training3 = persistTraining(generateTrainingWithActivityType(user1, "TENNIS"));

        mockMvc.perform(get("/api/trainings/activity").param("activity", "TENNIS").contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].userId").value(user1.getId()))
                .andExpect(jsonPath("$[0].activity").value(training2.getActivity()))
                .andExpect(jsonPath("$[1].userId").value(user1.getId()))
                .andExpect(jsonPath("$[1].activity").value(training3.getActivity()))
                .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void shouldPersistTraining_whenCreatingNewTraining() throws Exception {

        User user1 = existingUser(generateClient());

        String requestBody = """
                {
                    "userId": "%s",
                    "startTime": "2024-04-01T11:00:00",
                    "endTime": "2024-04-01T12:00:00",
                    "activity": "RUNNING",
                    "distance": 10.52
                }
                """.formatted(user1.getId());
        mockMvc.perform(post("/api/trainings").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(log())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(user1.getId()))
                .andExpect(jsonPath("$.startTime").value("2024-04-01T11:00:00"))
                .andExpect(jsonPath("$.endTime").value("2024-04-01T12:00:00"))
                .andExpect(jsonPath("$.activity").value("RUNNING"))
                .andExpect(jsonPath("$.distance").value(10.52));
    }

    @Test
    void shouldUpdateTraining_whenUpdatingTraining() throws Exception {

        User user1 = existingUser(generateClient());
        Training training1 = persistTraining(generateTrainingWithActivityType(user1, "RUNNING"));
        String requestBody = """
                {
                "userId": "%s",
                "startTime": "2022-04-01T10:00:00",
                "endTime": "2022-04-01T11:00:00",
                "activity": "TENNIS",
                "distance": 0.0
                }
                """.formatted(user1.getId());
        mockMvc.perform(put("/api/trainings/{trainingId}", training1.getId()).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(user1.getId()))
                .andExpect(jsonPath("$.startTime").value("2022-04-01T10:00:00"))
                .andExpect(jsonPath("$.endTime").value("2022-04-01T11:00:00"))
                .andExpect(jsonPath("$.activity").value("TENNIS"))
                .andExpect(jsonPath("$.distance").value(0.0));
    }

    private static User generateClient() {
        return new User(UUID.randomUUID().toString(), UUID.randomUUID().toString(), LocalDate.now(), UUID.randomUUID().toString());
    }

    private static Training generateTraining(User user) {
        return new Training(
                LocalDateTime.of(2024, 1, 19, 8, 0),
                LocalDateTime.of(2024, 1, 19, 9, 30),
                "RUNNING",
                10.5,
                user);
    }

    private static Training generateTrainingWithActivityType(User user, String activityType) {
        return new Training(
                LocalDateTime.of(2024, 1, 19, 8, 0),
                LocalDateTime.of(2024, 1, 19, 9, 30),
                activityType,
                0,
                user);
    }

    private static Training generateTrainingWithDetails(User user, String startTime, String endTime, String activityType, double distance) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return new Training(
                LocalDateTime.parse(startTime, formatter),
                LocalDateTime.parse(endTime, formatter),
                activityType,
                distance,
                user);
    }
}
