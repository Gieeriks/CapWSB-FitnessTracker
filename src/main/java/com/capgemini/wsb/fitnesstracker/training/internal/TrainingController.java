package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;

    @PostMapping
    public TrainingDto createTraining(@RequestBody TrainingDto trainingDto) {
        return trainingService.createTraining(trainingDto);
    }

    @GetMapping
    public List<TrainingDto> getAllTrainings() {
        return trainingService.findAllTrainings();
    }

    @GetMapping("/user/{userId}")
    public List<TrainingDto> getTrainingsByUserId(@PathVariable Long userId) {
        return trainingService.findTrainingsByUserId(userId);
    }

    @GetMapping("/before")
    public List<TrainingDto> getTrainingsBeforeEndTime(@RequestParam LocalDateTime endTime) {
        return trainingService.findTrainingsBeforeEndTime(endTime);
    }

    @GetMapping("/activity")
    public List<TrainingDto> getTrainingsByActivity(@RequestParam String activity) {
        return trainingService.findTrainingsByActivity(activity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainingDto> updateTraining(@PathVariable Long id, @RequestBody TrainingDto trainingDto) {
        trainingDto.setId(id);
        return ResponseEntity.ok(trainingService.updateTraining(trainingDto));
    }
}
