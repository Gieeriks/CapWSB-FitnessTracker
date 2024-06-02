package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.internal.TrainingDto;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingService;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.internal.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final UserRepository userRepository;

    @Override
    public TrainingDto createTraining(final TrainingDto trainingDto) {
        log.info("Creating Training {}", trainingDto);
        if (trainingDto.getId() != null) {
            throw new IllegalArgumentException("Training has already DB ID, update is not permitted!");
        }

        User user = userRepository.findById(trainingDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Training training = new Training(
                trainingDto.getStartTime(),
                trainingDto.getEndTime(),
                trainingDto.getActivity(),
                trainingDto.getDistance(),
                user
        );

        Training savedTraining = trainingRepository.save(training);
        return convertToDto(savedTraining);
    }

    @Override
    public List<TrainingDto> findAllTrainings() {
        return trainingRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingDto> findTrainingsByUserId(Long userId) {
        return trainingRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingDto> findTrainingsBeforeEndTime(LocalDateTime endTime) {
        return trainingRepository.findByEndTimeBefore(endTime).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingDto> findTrainingsByActivity(String activity) {
        return trainingRepository.findByActivity(activity).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TrainingDto> getTraining(Long trainingId) {
        return trainingRepository.findById(trainingId)
                .map(this::convertToDto);
    }

    @Override
    public TrainingDto updateTraining(TrainingDto trainingDto) {
        Training training = trainingRepository.findById(trainingDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Training not found"));

        User user = userRepository.findById(trainingDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        training.setActivity(trainingDto.getActivity());
        training.setDistance(trainingDto.getDistance());
        training.setStartTime(trainingDto.getStartTime());
        training.setEndTime(trainingDto.getEndTime());
        training.setUser(user);

        Training updatedTraining = trainingRepository.save(training);
        return convertToDto(updatedTraining);
    }

    @Override
    public List<Training> findTrainingsByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return trainingRepository.findByUserIdAndStartTimeBetween(userId, startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
    }

    private TrainingDto convertToDto(Training training) {
        return new TrainingDto(
                training.getId(),
                training.getActivity(),
                training.getDistance(),
                training.getStartTime(),
                training.getEndTime(),
                training.getUser().getId()
        );
    }
}
