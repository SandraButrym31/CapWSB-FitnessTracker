package com.capgemini.wsb.fitnesstracker.training.api;

import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;
import com.capgemini.wsb.fitnesstracker.user.api.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TrainingProvider {

    /**
     * Retrieves a training based on their ID.
     * If the user with given ID is not found, then {@link Optional#empty()} will be returned.
     *
     * @param trainingId id of the training to be searched
     * @return An {@link Optional} containing the located Training, or {@link Optional#empty()} if not found
     */
    Optional<User> getTraining(Long trainingId);

    void deleteByUserId(Long userId);

    public List<Training> getAllTrainings();

    public List<Training> getAllTrainingsByUserId(Long userId);

    public List<Training> getAllFinishedTrainings(LocalDateTime afterTime);

    public List<Training> getTrainingsByActivity(ActivityType activityType);

    public Training createTraining(TrainingDtoId trainingDtoId);

    public Training updateTraining(Long trainingId, Training updatedTraining);

}
