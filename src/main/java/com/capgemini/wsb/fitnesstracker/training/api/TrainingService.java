package com.capgemini.wsb.fitnesstracker.training.api;

import com.capgemini.wsb.fitnesstracker.training.internal.TrainingDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TrainingService {
    TrainingDto createTraining(TrainingDto trainingDto);
    List<TrainingDto> findAllTrainings();
    List<TrainingDto> findTrainingsByUserId(Long userId);
    List<TrainingDto> findTrainingsBeforeEndTime(LocalDateTime endTime);
    List<TrainingDto> findTrainingsByActivity(String activity);
    Optional<TrainingDto> getTraining(Long trainingId);
    TrainingDto updateTraining(TrainingDto trainingDto);
    List<Training> findTrainingsByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);
}
