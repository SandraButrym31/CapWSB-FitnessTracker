package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/trainings")
public class TrainingController {

  @Autowired
  private TrainingServiceImpl trainingService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Training> getAllTrainings() {
    return trainingService.getAllTrainings();
  }

  @GetMapping("/{userId}")
  @ResponseStatus(HttpStatus.OK)
  public List<Training> getAllTrainingsForUser(@PathVariable Long userId) {
    return trainingService.getAllTrainingsByUserId(userId);
  }

  @GetMapping("/finished/{afterTime}")
  @ResponseStatus(HttpStatus.OK)
  public List<Training> getAllFinishedTrainingsAfter(@PathVariable String afterTime) {
    LocalDateTime afterDateTime = LocalDate.parse(afterTime).atStartOfDay();
    return trainingService.getAllFinishedTrainings(afterDateTime);
  }

  @GetMapping("/activityType")
  @ResponseStatus(HttpStatus.OK)
  public List<Training> getAllTrainingByActivityType(@RequestParam ActivityType activityType) {
    return trainingService.getTrainingsByActivity(activityType);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Training createTraining(@RequestBody TrainingRequest trainingRequest) {
    return trainingService.createTraining(trainingRequest);
  }

  @PutMapping("/{trainingId}")
  @ResponseStatus(HttpStatus.OK)
  public Training updateTraining(@PathVariable Long trainingId, @RequestBody Training training) {
    return trainingService.updateTraining(trainingId, training);
  }
}

