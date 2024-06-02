package com.capgemini.wsb.fitnesstracker.report;

import com.capgemini.wsb.fitnesstracker.notification.EmailService;
import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingService;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.internal.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingReportService {

    private final UserRepository userRepository;
    private final TrainingService trainingService;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 0 * * MON")  // Co poniedziałek o północy
    public void sendWeeklyReports() {
        List<User> users = userRepository.findAll();
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        for (User user : users) {
            List<Training> trainings = trainingService.findTrainingsByUserIdAndDateRange(user.getId(), startOfWeek, endOfWeek);
            String report = generateReport(user, trainings);
            emailService.sendSimpleMessage(user.getEmail(), "Weekly Training Report", report);
        }
    }

    private String generateReport(User user, List<Training> trainings) {
        StringBuilder report = new StringBuilder();
        report.append("Hello ").append(user.getFirstName()).append(",\n\n");
        report.append("Here is your training report for the week:\n\n");

        for (Training training : trainings) {
            report.append("Activity: ").append(training.getActivity()).append("\n")
                    .append("Distance: ").append(training.getDistance()).append(" km\n")
                    .append("Start Time: ").append(training.getStartTime()).append("\n")
                    .append("End Time: ").append(training.getEndTime()).append("\n\n");
        }

        report.append("Total Trainings: ").append(trainings.size()).append("\n");
        report.append("Keep up the good work!\n\n");
        report.append("Best regards,\nYour FitnessTracker Team");

        return report.toString();
    }
}
